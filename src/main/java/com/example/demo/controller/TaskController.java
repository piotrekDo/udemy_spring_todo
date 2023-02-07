package com.example.demo.controller;

import com.example.demo.logic.TaskService;
import com.example.demo.model.Task;
import com.example.demo.model.TaskRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@RestController()
@RequestMapping("/tasks")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository taskRepository;
    private final TaskService taskService;

    public TaskController(TaskRepository repository, TaskService taskService) {
        this.taskRepository = repository;
        this.taskService = taskService;
    }

    @RequestMapping(method = RequestMethod.GET, params = {"!sort", "!page", "!size"})
    CompletableFuture<ResponseEntity<List<Task>>> readAllTasks(){
        logger.warn("Exposing all the tasks");
        return taskService.findAllAsync().thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    ResponseEntity<Task> readTask(@PathVariable int id) {
        return taskRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/done")
    ResponseEntity<List<Task>> readDoneTasks(@RequestParam(defaultValue = "true", required = false) boolean state) {
        return ResponseEntity.ok(
                taskRepository.findAllByDone(state)
        );
    }

    @GetMapping()
    ResponseEntity<Page<Task>> readAllTasks(Pageable page) {
        logger.warn("Custom pageable");
        return ResponseEntity.ok(taskRepository.findAll(page));
    }

    @Transactional
    @PutMapping("/{id}")
    ResponseEntity<?> updateTask(@PathVariable("id") int taskId, @RequestBody @Valid Task toUpdate) {
        if (!taskRepository.existsById(taskId)) {
            return ResponseEntity.notFound().build();
        }
        taskRepository.findById(taskId).ifPresent(task -> task.updateFrom(toUpdate));
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    ResponseEntity<?> addNewTask(@RequestBody Task task) {
        taskRepository.save(task);
        return ResponseEntity.ok().build();
    }

    @Transactional()
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable int id) {
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        taskRepository.findById(id).ifPresent(task -> task.setDone(!task.isDone()));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/today")
    public ResponseEntity<List<Task>> findTasksForTodayOrPastDue(){
        return ResponseEntity.ok(taskService.findTasksForTodayOrPastDue());
    }

}
