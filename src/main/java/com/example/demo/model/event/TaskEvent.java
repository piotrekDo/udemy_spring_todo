package com.example.demo.model.event;

import com.example.demo.model.Task;

import java.time.Clock;
import java.time.Instant;

public abstract class TaskEvent {

    private int taskId;
    private Instant occurrence;

    TaskEvent(int taskId, Clock clock) {
        this.taskId = taskId;
        this.occurrence = Instant.now(clock);
    }

    public  static TaskEvent changed(Task source){
       return source.isDone() ? new TaskDone(source) : new TaskUndone(source);
    }

    public int getTaskId() {
        return taskId;
    }

    public Instant getOccurrence() {
        return occurrence;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " {" +
                "taskId=" + taskId +
                ", occurrence=" + occurrence +
                '}';
    }
}
