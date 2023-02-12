package com.example.demo.raport;

import com.example.demo.model.TaskRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/reports")
public class ReportController {

    private final TaskRepository taskRepository;
    private final PersistedTaskEventRepository persistedTaskEventRepository;

    public ReportController(TaskRepository taskRepository, PersistedTaskEventRepository persistedTaskEventRepository) {
        this.taskRepository = taskRepository;
        this.persistedTaskEventRepository = persistedTaskEventRepository;
    }

    @GetMapping("/count/{id}")
    ResponseEntity<TaskWithChangesCount> readTaskWithCount(@PathVariable int id){
        return taskRepository.findById(id)
                .map(task -> new TaskWithChangesCount(task, persistedTaskEventRepository.findAllByTaskId(id)))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
