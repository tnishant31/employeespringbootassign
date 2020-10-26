package com.nishant.assignment.springassignment.controllers;

import com.nishant.assignment.springassignment.entities.Department;
import com.nishant.assignment.springassignment.exceptions.DoesNotExistsException;
import com.nishant.assignment.springassignment.services.DepartmentRepository;
import com.nishant.assignment.springassignment.services.EmployeeRepository;
import com.nishant.assignment.springassignment.specifications.DepartmentSearchSpecification;
import com.sipios.springsearch.anotation.SearchSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/assignment/departments")
public class DepartmentController {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    Logger LOG = LoggerFactory.getLogger(DepartmentController.class);
    private DepartmentSearchSpecification departmentSearchSpecification;

    @Autowired
    public DepartmentController(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    // create a department
    @PostMapping
    public ResponseEntity<Department> createDepartment(@Valid @RequestBody Department department) {
        Department dept = departmentRepository.save(department);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{deptId}").buildAndExpand(dept.getDeptId()).toUri();
        LOG.info("Department created successfully :)");

        return ResponseEntity.created(location).body(dept);
    }

    // read a department
    @GetMapping("/{deptId}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Integer deptId) {
        Optional<Department> optionalDepartment = departmentRepository.findById(deptId);

        return optionalDepartment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.unprocessableEntity().build());
    }

    // read all departments
    @GetMapping
    public ResponseEntity<Page<Department>> getAllDepartments(Pageable pageable) {
        return ResponseEntity.ok(departmentRepository.findAll(pageable));
    }

    // update a department
    @PutMapping("/{deptId}")
    public ResponseEntity<Department> updateDepartment(@PathVariable Integer deptId, @Valid @RequestBody Department department) {
        Optional<Department> optionalDepartment = departmentRepository.findById(deptId);

        if (optionalDepartment.isEmpty()) {
            LOG.error("Department with Id " + deptId + " does not exists!");
            throw new DoesNotExistsException("Department does not exists");
            // return ResponseEntity.unprocessableEntity().build();
        } else {
            department.setDeptId(optionalDepartment.get().getDeptId());
            departmentRepository.save(department);
            LOG.info("Department updated successfully :)");

            return ResponseEntity.noContent().build();
        }
    }

    // delete a department
    @DeleteMapping("/{deptId}")
    public ResponseEntity<Department> deleteDepartment(@PathVariable Integer deptId) {
        Optional<Department> optionalDepartment = departmentRepository.findById(deptId);

        if (optionalDepartment.isEmpty()) {
            LOG.error("Department with Id " + deptId + " does not exists!");
            throw new DoesNotExistsException("Department does not exists");
            // return ResponseEntity.unprocessableEntity().build();
        } else {
            departmentRepository.delete(optionalDepartment.get());
            LOG.info("Department deleted successfully :)");

            return ResponseEntity.noContent().build();
        }
    }

    // search api
    @GetMapping("/search/department")
    public ResponseEntity<List<Department>> searchForDepartments(@SearchSpec Specification<Department> departmentSpecification) {
        return new ResponseEntity<>(departmentSearchSpecification.findAll(Specification.where(departmentSpecification)), HttpStatus.OK);
    }
}
