package com.nishant.assignment.springassignment.controllers;

import com.nishant.assignment.springassignment.entities.Department;
import com.nishant.assignment.springassignment.entities.Employee;
import com.nishant.assignment.springassignment.exceptions.DoesNotExistsException;
import com.nishant.assignment.springassignment.services.DepartmentRepository;
import com.nishant.assignment.springassignment.services.EmployeeRepository;
import com.nishant.assignment.springassignment.services.TaskRepository;
import com.nishant.assignment.springassignment.specifications.EmployeeSearchSpecification;
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
@RequestMapping("/assignment/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    Logger LOG = LoggerFactory.getLogger(EmployeeController.class);
    private final TaskRepository taskRepository;
    private final EmployeeSearchSpecification employeeSearchSpecification;

    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, TaskRepository taskRepository, EmployeeSearchSpecification employeeSearchSpecification) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.taskRepository = taskRepository;
        this.employeeSearchSpecification = employeeSearchSpecification;
    }

    // create employee
    @PostMapping("/employee")
    public ResponseEntity<Employee> createdEmployee(@Valid @RequestBody Employee employee) {
        try {
            Optional<Department> optionalDepartment = departmentRepository.findById(employee.getDepartment().getDeptId());
            employee.setDepartment(optionalDepartment.get());
        } catch (Exception e) {
            LOG.debug("The error is: ", e);
        }
        Employee newEmployee = employeeRepository.save(employee);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{empId}").buildAndExpand(newEmployee.getEmpId()).toUri();
        LOG.info("Employee (without department) successfully :)");

        return ResponseEntity.created(location).body(newEmployee);
    }

    // create employee and assign a department
    @PostMapping
    public ResponseEntity<Employee> createEmployeeAndAddToADepartment(@Valid @RequestBody Employee employee, @RequestBody Department department) {
        employee.setDepartment(department);
        LOG.error("New error " + departmentRepository.findById(employee.getDepartment().getDeptId()));
        Optional<Department> optionalDepartment = departmentRepository.findById(employee.getDepartment().getDeptId());

        if (optionalDepartment.isEmpty()) {
            LOG.error("Employee creation failed");
            throw new DoesNotExistsException("Employee creation failed");
            // return ResponseEntity.unprocessableEntity().build();
        } else {
            employee.setDepartment(optionalDepartment.get());

            Employee newEmployee = employeeRepository.save(employee);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{empId}").buildAndExpand(newEmployee.getEmpId()).toUri();
            LOG.info("Employee created successfully :)");

            return ResponseEntity.created(location).body(newEmployee);
        }
    }

    // read an employee
    @GetMapping("/{empId}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Integer empId) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(empId);

        if (optionalEmployee.isEmpty()) {
            LOG.error("Employee with Id " + empId + " does not exist!");
            throw new DoesNotExistsException("Employee does not exists");
            // return ResponseEntity.unprocessableEntity().build();
        } else {
            LOG.info("Employee fetched successfully :)");
            return ResponseEntity.ok(optionalEmployee.get());
        }
    }

    // read all employees
    @GetMapping
    public ResponseEntity<Page<Employee>> getAllEmployees(Pageable pageable) {
        return ResponseEntity.ok(employeeRepository.findAll(pageable));
    }

    // update an employee
    @PutMapping("/{empId}/{deptId}")
    public ResponseEntity<Employee> updateEmployeeWithDepartment(@Valid @RequestBody Employee employee, @PathVariable Integer empId, @PathVariable Integer deptId) {
        LOG.debug(deptId.toString());
        Optional<Department> optionalDepartment = departmentRepository.findById(deptId);

        if (optionalDepartment.isEmpty()) {
            LOG.error("Department with Id " + employee.getDepartment().getDeptId() + " does not exits!");
            throw new DoesNotExistsException("Department does not exists");
            // return ResponseEntity.unprocessableEntity().build();
        } else {
            Optional<Employee> optionalEmployee = employeeRepository.findById(empId);

            if (optionalEmployee.isEmpty()) {
                LOG.error("Employee with Id " + empId + " does not exists!");
                throw new DoesNotExistsException("Employee does not exists");
                //return ResponseEntity.unprocessableEntity().build();
            } else {
                employee.setDepartment(optionalDepartment.get());
                employee.setEmpId(optionalEmployee.get().getEmpId());
                employeeRepository.save(employee);
                LOG.info("Employee updated successfully :)");

                return ResponseEntity.noContent().build();
            }
        }
    }

    // delete an employee
    @DeleteMapping("/{empId}")
    public ResponseEntity<Employee> deleteEmployeeFromDepartment(@PathVariable Integer empId) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(empId);

        if (optionalEmployee.isEmpty()) {
            LOG.error("Employee with Id " + empId + " does not exits!");
            throw new DoesNotExistsException("Employee does not exists");
            //return ResponseEntity.unprocessableEntity().build();
        } else {
            employeeRepository.delete(optionalEmployee.get());
            LOG.info("Employee deleted successfully :)");

            return ResponseEntity.noContent().build();
        }
    }

    // search api
    @GetMapping("/search/employee")
    public ResponseEntity<List<Employee>> searchForEmployees(@SearchSpec Specification<Employee> employeeSpecification) {
        return new ResponseEntity<>(employeeSearchSpecification.findAll(Specification.where(employeeSpecification)), HttpStatus.OK);
    }
}