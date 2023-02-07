package com.example.demo.logic;

import com.example.demo.model.Task;
import com.example.demo.model.TaskRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Async()
    public CompletableFuture<List<Task>> findAllAsync() {
        return CompletableFuture.supplyAsync(taskRepository::findAll);
    }
}
