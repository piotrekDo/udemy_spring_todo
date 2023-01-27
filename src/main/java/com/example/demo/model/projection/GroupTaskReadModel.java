package com.example.demo.model.projection;

import com.example.demo.model.Task;

import java.util.Objects;

public class GroupTaskReadModel {
    private int id;
    private boolean done;
    private String description;

    public GroupTaskReadModel(Task task) {
        this.id = task.getId();
        this.description = task.getDescription();
        this.done = task.isDone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupTaskReadModel that = (GroupTaskReadModel) o;
        return done == that.done && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(done, description);
    }

    @Override
    public String toString() {
        return "GroupTaskReadModel{" +
                "done=" + done +
                ", description='" + description + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
