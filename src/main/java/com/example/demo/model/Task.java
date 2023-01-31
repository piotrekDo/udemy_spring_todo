package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "description cannot be blank")
    private String description;
    private boolean done;
    private LocalDateTime deadline;
    @JsonIgnore
    private LocalDateTime createdOn;
    @JsonIgnore
    private LocalDateTime updatedOn;

    public Task() {
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

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public void updateFrom(final Task source) {
        this.description = source.description;
        this.done = source.done;
        this.deadline = source.deadline;
    }



    @PrePersist
    void prePersist() {
        this.createdOn = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        this.updatedOn = LocalDateTime.now();
    }
}
