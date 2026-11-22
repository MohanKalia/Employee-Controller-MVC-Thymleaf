package com.example.springboot.ThymeleafMVC.controller;

import com.example.springboot.ThymeleafMVC.entity.Employee;
import com.example.springboot.ThymeleafMVC.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private static final int PAGE_SIZE = 10;
    private static final String FORM_VIEW = "employees/employee-form";
    private static final String LIST_REDIRECT = "redirect:/employees/list";

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/list")
    public String listEmployees(@RequestParam(defaultValue = "") String q,
                                @RequestParam(defaultValue = "1") int page,
                                Model model, RedirectAttributes redirect) {
        if (page < 1 || page - 1 > Integer.MAX_VALUE / PAGE_SIZE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid page number");
        }
        String query = q.strip();
        Page<Employee> employees = employeeService.search(query,
                PageRequest.of(page - 1, PAGE_SIZE, Sort.by("lastName", "firstName", "id")));
        int lastPage = Math.max(1, employees.getTotalPages());
        if (page > lastPage) {
            redirect.addAttribute("page", lastPage);
            redirect.addAttribute("q", query);
            return LIST_REDIRECT;
        }

        model.addAttribute("employees", employees);
        model.addAttribute("query", query);
        return "employees/list-employees";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model model) {
        model.addAttribute("employee", new EmployeeForm("", "", ""));
        return FORM_VIEW;
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam int employeeId, Model model) {
        model.addAttribute("employee", EmployeeForm.from(employeeService.findById(employeeId)));
        model.addAttribute("employeeId", employeeId);
        return FORM_VIEW;
    }

    @PostMapping("/save")
    public String saveEmployee(@RequestParam(required = false) Integer employeeId,
                               @Valid @ModelAttribute("employee") EmployeeForm employee,
                               BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            if (employeeId != null) {
                employeeService.findById(employeeId);
                model.addAttribute("employeeId", employeeId);
            }
            return FORM_VIEW;
        }

        if (employeeId == null) {
            employeeService.create(employee.firstName(), employee.lastName(), employee.email());
        } else {
            employeeService.update(employeeId, employee.firstName(), employee.lastName(), employee.email());
        }
        return LIST_REDIRECT;
    }

    @PostMapping("/delete")
    public String deleteEmployee(@RequestParam int employeeId) {
        employeeService.deleteById(employeeId);
        return LIST_REDIRECT;
    }
}
