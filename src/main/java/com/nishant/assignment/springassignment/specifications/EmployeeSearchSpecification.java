package com.nishant.assignment.springassignment.specifications;

import com.nishant.assignment.springassignment.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface EmployeeSearchSpecification extends JpaRepository<Employee, Integer>, JpaSpecificationExecutor<Employee> {

}
