package com.example.springboot.ThymeleafMVC.service;

import com.example.springboot.ThymeleafMVC.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

    Page<Employee> search(String query, Pageable pageable);

    Employee findById(int id);

    void create(String firstName, String lastName, String email);

    void update(int id, String firstName, String lastName, String email);

    void deleteById(int id);
}
