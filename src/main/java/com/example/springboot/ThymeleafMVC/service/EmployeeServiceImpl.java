package com.example.springboot.ThymeleafMVC.service;

import com.example.springboot.ThymeleafMVC.dao.EmployeeRepository;
import com.example.springboot.ThymeleafMVC.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Page<Employee> search(String query, Pageable pageable) {
        if (query.isBlank()) {
            return employeeRepository.findAll(pageable);
        }
        return employeeRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                        query, query, query, pageable);
    }

    @Override
    public Employee findById(int id) {
        return employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @Override
    public void create(String firstName, String lastName, String email) {
        employeeRepository.save(new Employee(firstName, lastName, email));
    }

    @Override
    @Transactional
    public void update(int id, String firstName, String lastName, String email) {
        Employee employee = findById(id);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmail(email);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        employeeRepository.delete(findById(id));
    }
}
