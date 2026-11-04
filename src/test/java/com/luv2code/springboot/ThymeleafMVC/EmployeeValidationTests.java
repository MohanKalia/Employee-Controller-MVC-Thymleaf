package com.luv2code.springboot.ThymeleafMVC;

import com.luv2code.springboot.ThymeleafMVC.entity.Employee;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
        Employee employee = new Employee(" ", "", "");

        assertFalse(validator.validate(employee).isEmpty());
    }

    @Test
    void rejectsInvalidEmail() {
        Employee employee = new Employee("Sam", "Taylor", "not-an-email");

        assertTrue(validator.validate(employee).stream()
                .anyMatch(error -> error.getPropertyPath().toString().equals("email")));
    }

    @Test
    void acceptsValidEmployee() {
        Employee employee = new Employee("Sam", "Taylor", "sam.taylor@example.com");

        assertTrue(validator.validate(employee).isEmpty());
    }
}
