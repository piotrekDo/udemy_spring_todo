package com.example.demo.model.projection;

import com.example.demo.model.Project;
import com.example.demo.model.ProjectStep;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.List;

public class ProjectWriteModel {
    @NotBlank(message = "Project's description must not be empty")
    private String description;
    @Valid
    private List<ProjectStep> steps;

    public Project toProject(){
        Project result = new Project();
        result.setDescription(this.description);
        this.steps.forEach(step -> step.setProject(result));
        result.setProjectSteps(new HashSet<>(this.steps));
        return result;
    }

    public ProjectWriteModel() {
    }

    public ProjectWriteModel(String description, List<ProjectStep> steps) {
        this.description = description;
        this.steps = steps;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ProjectStep> getSteps() {
        return steps;
    }

    public void setSteps(List<ProjectStep> steps) {
        this.steps = steps;
    }
}
