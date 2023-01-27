package com.example.demo.controller;

import com.example.demo.logic.TaskGroupService;
import com.example.demo.model.Task;
import com.example.demo.model.projection.GroupReadModel;
import com.example.demo.model.projection.GroupWriteModel;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task-groups")
public class TaskGroupController {
    private final TaskGroupService taskGroupService;

    public TaskGroupController(TaskGroupService taskGroupService) {
        this.taskGroupService = taskGroupService;
    }

    @PostMapping
    ResponseEntity<GroupReadModel> createNewGroup(@RequestBody @Valid GroupWriteModel taskGroup) {
        GroupReadModel group = taskGroupService.createGroup(taskGroup);
        return ResponseEntity.status(HttpStatus.CREATED).body(group);
    }

    @GetMapping
    ResponseEntity<List<GroupReadModel>> findAll(){
        return ResponseEntity.ok(taskGroupService.readAll());
    }

    @PatchMapping("/toggle/{id}")
    ResponseEntity<?> toggleGroup(@PathVariable int id) {
        taskGroupService.toggleGroup(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/tasks")
    ResponseEntity<List<Task>> getTasksByGroup(@PathVariable int id) {
        return ResponseEntity.ok(taskGroupService.getTasksByGroup(id));
    }

}
