package com.example.demo.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.util.Set;

@Entity
@Table(name="task_groups")
public class TaskGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Task group's description must not be empty")
    private String description;
    private boolean done;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "taskGroup")
    private Set<Task> tasks;
    @ManyToOne
    private Project project;

    public TaskGroup() {
    }

    private TaskGroup(String description, Set<Task> tasks) {
        this.description = description;
        this.tasks = tasks;
    }

    public static TaskGroup CreateNewTaskGroup(String description, Set<Task> tasks) {
        return new TaskGroup(description, tasks);
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

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
