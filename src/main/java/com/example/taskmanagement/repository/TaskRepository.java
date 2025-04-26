package com.example.taskmanagement.repository;

import com.example.taskmanagement.entity.Task;
import com.example.taskmanagement.entity.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByStatus(TaskStatus status, Pageable pageable);
    Page<Task> findByDueDate(LocalDate dueDate, Pageable pageable);
    Page<Task> findByStatusAndDueDate(TaskStatus status, LocalDate dueDate, Pageable pageable);
}