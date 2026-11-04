package com.example.springboot.ThymeleafMVC.dao;

import com.example.springboot.ThymeleafMVC.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    List<Employee> findAllByOrderByLastNameAsc();

}
