package com.example.demo.raport;

import com.example.demo.model.event.TaskEvent;
import javax.persistence.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "task_events")
public class PersistedTaskEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int taskId;
    String name;
    LocalDateTime occurrence;

    public PersistedTaskEvent() {
    }

    public PersistedTaskEvent(TaskEvent source) {
        this.taskId = source.getTaskId();
        this.name = source.getClass().getSimpleName();
        this.occurrence = LocalDateTime.ofInstant(source.getOccurrence(), ZoneId.systemDefault());
    }
}
