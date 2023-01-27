package com.example.demo.model.projection;

import com.example.demo.model.Task;
import com.example.demo.model.TaskGroup;

import java.time.LocalDateTime;

public class GroupTaskWriteModel {

    private String description;
    private LocalDateTime deadline;

    public Task toTask(TaskGroup group) {
        return new Task(this.description, this.deadline, group);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }
}
