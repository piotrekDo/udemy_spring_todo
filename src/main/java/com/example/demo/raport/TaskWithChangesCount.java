package com.example.demo.raport;

import com.example.demo.model.Task;

import java.util.List;

public class TaskWithChangesCount {

    public String description;
    public boolean done;
    public int changesCount;

    public TaskWithChangesCount(Task task, List<PersistedTaskEvent> events) {
        this.description = task.getDescription();
        this.done = task.isDone();
        this.changesCount = events.size();
    }
}
