package com.nishant.assignment.springassignment.controllers;

import com.nishant.assignment.springassignment.entities.Employee;
import com.nishant.assignment.springassignment.entities.Task;
import com.nishant.assignment.springassignment.exceptions.DoesNotExistsException;
import com.nishant.assignment.springassignment.services.EmployeeRepository;
import com.nishant.assignment.springassignment.services.TaskRepository;
import com.nishant.assignment.springassignment.specifications.TaskSearchSpecification;
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
@RequestMapping("/assignment/tasks")
public class TaskController {

    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;
    Logger LOG = LoggerFactory.getLogger(TaskController.class);
    private TaskSearchSpecification taskSearchSpecification;

    @Autowired
    public TaskController(TaskRepository taskRepository, EmployeeRepository employeeRepository) {
        this.taskRepository = taskRepository;
        this.employeeRepository = employeeRepository;
    }

    // create a task and assign it to the employee
    @PostMapping
    public ResponseEntity<Task> createTaskAndAddToEmployee(@Valid @RequestBody Task task) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(task.getEmployee().getEmpId());

        if (optionalEmployee.isEmpty()) {
            LOG.error("Task creation failed");
            throw new DoesNotExistsException("Task creation failed");
            //return ResponseEntity.unprocessableEntity().build();
        } else {
            task.setEmployee(optionalEmployee.get());

            Task newTask = taskRepository.save(task);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{taskId}").buildAndExpand(newTask.getTaskId()).toUri();
            LOG.info("Task created successfully");

            return ResponseEntity.created(location).body(newTask);
        }
    }

    // read a task
    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Integer taskId) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);

        if (optionalTask.isEmpty()) {
            LOG.error("Task with Id " + taskId + " does not exits");
            throw new DoesNotExistsException("Task does not exists");
            //return ResponseEntity.unprocessableEntity().build();
        } else {
            LOG.info("Task fetched successfully");
            return ResponseEntity.ok(optionalTask.get());
        }
    }

    // read all tasks
    @GetMapping
    public ResponseEntity<Page<Task>> getAllTasks(Pageable pageable) {
        return ResponseEntity.ok(taskRepository.findAll(pageable));
    }

    // update a task of an employee
    @PutMapping("/{taskId}/{empId}")
    public ResponseEntity<Task> updateTaskOfEmployee(@Valid @RequestBody Task task, @PathVariable Integer taskId, @PathVariable Integer empId) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(empId);

        if (optionalEmployee.isEmpty()) {
            LOG.error("Employee with Id " + empId + " does not exists!");
            throw new DoesNotExistsException("Employee does not exists");
            // return ResponseEntity.unprocessableEntity().build();
        } else {
            Optional<Task> optionalTask = taskRepository.findById(taskId);

            if (optionalTask.isEmpty()) {
                LOG.error("Task with Id " + taskId + " does not exists!");
                throw new DoesNotExistsException("Task does not exists");
                //return ResponseEntity.unprocessableEntity().build();
            } else {
                task.setEmployee(optionalEmployee.get());
                task.setTaskId(optionalTask.get().getTaskId());
                taskRepository.save(task);
                LOG.info("Task saved successfully");

                return ResponseEntity.noContent().build();
            }
        }
    }

    // delete a task
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Task> deleteTaskOfEmployee(@PathVariable Integer taskId) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);

        if (optionalTask.isEmpty()) {
            LOG.error("Task with Id " + taskId + " does not exists");
            throw new DoesNotExistsException("Task does not exists");
            // return ResponseEntity.noContent().build();
        } else {
            taskRepository.delete(optionalTask.get());
            LOG.info("Task deleted successfully");

            return ResponseEntity.noContent().build();
        }
    }

    // search api
    @GetMapping("/search/task")
    public ResponseEntity<List<Task>> searchForTasks(@SearchSpec Specification<Task> taskSpecification) {
        return new ResponseEntity<>(taskSearchSpecification.findAll(Specification.where(taskSpecification)), HttpStatus.OK);
    }
}
