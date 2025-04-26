package com.example.taskmanagement.service;

import com.example.taskmanagement.entity.Task;
import com.example.taskmanagement.entity.TaskStatus;
import com.example.taskmanagement.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Page<Task> getUserTasks(String status, LocalDate dueDate, Pageable pageable) {
        if (status != null && dueDate != null) {
            return taskRepository.findByStatusAndDueDate(TaskStatus.valueOf(status), dueDate, pageable);
        } else if (status != null) {
            return taskRepository.findByStatus(TaskStatus.valueOf(status), pageable);
        } else if (dueDate != null) {
            return taskRepository.findByDueDate(dueDate, pageable);
        }
        return taskRepository.findAll(pageable);
    }

    public Task updateTask(Long id, Task task) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setStatus(task.getStatus());
        existingTask.setDueDate(task.getDueDate());
        return taskRepository.save(existingTask);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}