package com.example.springboot.ThymeleafMVC;

import com.example.springboot.ThymeleafMVC.controller.EmployeeForm;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmployeeValidationTests {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void closeValidatorFactory() {
        validatorFactory.close();
    }

    @Test
    void rejectsBlankNamesAndEmail() {
        EmployeeForm employee = new EmployeeForm(" ", "", "");

        assertEquals(Set.of("firstName", "lastName", "email"), validator.validate(employee).stream()
                .map(error -> error.getPropertyPath().toString())
                .collect(Collectors.toSet()));
    }

    @Test
    void rejectsInvalidEmail() {
        EmployeeForm employee = new EmployeeForm("Sam", "Taylor", "not-an-email");

        assertTrue(validator.validate(employee).stream()
                .anyMatch(error -> error.getPropertyPath().toString().equals("email")));
    }

    @Test
    void acceptsValidEmployee() {
        EmployeeForm employee = new EmployeeForm("Sam", "Taylor", "sam.taylor@example.com");

        assertTrue(validator.validate(employee).isEmpty());
    }
}
