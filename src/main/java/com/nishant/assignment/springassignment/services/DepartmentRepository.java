package com.nishant.assignment.springassignment.services;

import com.nishant.assignment.springassignment.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}
