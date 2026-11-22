package com.example.springboot.ThymeleafMVC.controller;

import com.example.springboot.ThymeleafMVC.entity.Employee;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EmployeeForm(
        @NotBlank(message = "First name is required")
        @Size(max = Employee.FIELD_LENGTH, message = "First name must be at most 45 characters")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(max = Employee.FIELD_LENGTH, message = "Last name must be at most 45 characters")
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Please enter a valid email address")
        @Size(max = Employee.FIELD_LENGTH, message = "Email must be at most 45 characters")
        String email) {

    static EmployeeForm from(Employee employee) {
        return new EmployeeForm(employee.getFirstName(), employee.getLastName(), employee.getEmail());
    }
}
