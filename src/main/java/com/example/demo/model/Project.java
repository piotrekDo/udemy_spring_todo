package com.example.demo.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.util.Set;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Project's description must not be empty")
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    private Set<TaskGroup> taskGroups;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    private Set<ProjectStep> projectSteps;

    public Project() {
    }

    public Project(int id, String description) {
        this.id = id;
        this.description = description;
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

    public Set<TaskGroup> getTaskGroups() {
        return taskGroups;
    }

    public void setTaskGroups(Set<TaskGroup> taskGroups) {
        this.taskGroups = taskGroups;
    }

    public Set<ProjectStep> getProjectSteps() {
        return projectSteps;
    }

    public void setProjectSteps(Set<ProjectStep> projectSteps) {
        this.projectSteps = projectSteps;
    }
}


