package com.example.demo.model.projection;

import com.example.demo.model.Task;
import com.example.demo.model.TaskGroup;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupReadModel {

    private int id;
    private String description;
    /**
     * Deadline from the latest task on group.
     */
    private LocalDateTime deadline;
    private boolean done;
    private Set<GroupTaskReadModel> tasks;

    public GroupReadModel(TaskGroup source) {
        this.id = source.getId();
        this.description = source.getDescription();
        this.deadline = source.getTasks().stream()
                .map(Task::getDeadline)
                .max(LocalDateTime::compareTo)
                .orElse(null);
        this.done = source.isDone();
        this.tasks = source.getTasks().stream()
                .map(GroupTaskReadModel::new)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupReadModel that = (GroupReadModel) o;
        return Objects.equals(description, that.description) && Objects.equals(deadline, that.deadline) && Objects.equals(tasks, that.tasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, deadline, tasks);
    }

    @Override
    public String toString() {
        return "GroupReadModel{" +
                "description='" + description + '\'' +
                ", deadline=" + deadline +
                ", tasks=" + tasks +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Set<GroupTaskReadModel> getTasks() {
        return tasks;
    }

    public void setTasks(Set<GroupTaskReadModel> tasks) {
        this.tasks = tasks;
    }
}
