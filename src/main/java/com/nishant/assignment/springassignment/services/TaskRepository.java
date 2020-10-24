package com.nishant.assignment.springassignment.services;

import com.nishant.assignment.springassignment.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface TaskRepository extends JpaRepository<Task, Integer> {
}
