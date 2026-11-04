package com.example.springboot.ThymeleafMVC;

import com.example.springboot.ThymeleafMVC.controller.EmployeeController;
import com.example.springboot.ThymeleafMVC.entity.Employee;
import com.example.springboot.ThymeleafMVC.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.validation.BindingResult;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTests {

    @Mock
    private EmployeeService employeeService;

    @Mock
    private BindingResult bindingResult;

    @Test
    void invalidEmployeeReturnsToFormWithoutSaving() {
        EmployeeController controller = new EmployeeController(employeeService);
        Employee employee = new Employee("", "Taylor", "not-an-email");
        when(bindingResult.hasErrors()).thenReturn(true);

        String view = controller.saveEmployee(employee, bindingResult);

        assertEquals("employees/employee-form", view);
        verify(employeeService, never()).save(employee);
    }
}
