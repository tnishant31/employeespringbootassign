package com.nishant.assignment.springassignment.specifications;

import com.nishant.assignment.springassignment.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface DepartmentSearchSpecification extends JpaRepository<Department, Integer>, JpaSpecificationExecutor<Department> {
}
