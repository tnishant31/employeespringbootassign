package com.nishant.assignment.springassignment.services;

import com.nishant.assignment.springassignment.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
