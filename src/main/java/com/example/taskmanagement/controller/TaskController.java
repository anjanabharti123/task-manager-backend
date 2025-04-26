package com.example.taskmanagement.controller;

import com.example.taskmanagement.entity.Task;
import com.example.taskmanagement.entity.TaskStatus;
import com.example.taskmanagement.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Task API", description = "APIs for managing tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Operation(summary = "Create a new task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid task data"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody TaskDTO taskDTO) {
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(TaskStatus.valueOf(taskDTO.getStatus()));
        task.setDueDate(taskDTO.getDueDate());
        Task createdTask = taskService.createTask(task);
        return ResponseEntity.ok(createdTask);
    }

    @Operation(summary = "Get all tasks with pagination, sorting, and filtering")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved tasks"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    @GetMapping
    public ResponseEntity<Page<Task>> getUserTasks(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) LocalDate dueDate,
            @RequestParam(defaultValue = "id,asc") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        String[] sortParams = sort.split(",");
        Sort sortOrder = Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        Page<Task> tasks = taskService.getUserTasks(status, dueDate, pageable);
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Update an existing task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid task data"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody TaskDTO taskDTO) {
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(TaskStatus.valueOf(taskDTO.getStatus()));
        task.setDueDate(taskDTO.getDueDate());
        Task updatedTask = taskService.updateTask(id, task);
        return ResponseEntity.ok(updatedTask);
    }

    @Operation(summary = "Delete a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok("Task deleted");
    }

    @Operation(summary = "Test endpoint")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Test endpoint working")
    })
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test endpoint working!");
    }
}

class TaskDTO {
    @NotBlank
    private String title;
    private String description;
    @NotBlank
    private String status;
    private LocalDate dueDate;

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
}