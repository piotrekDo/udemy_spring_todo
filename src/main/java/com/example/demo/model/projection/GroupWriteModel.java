package com.example.demo.model.projection;

import com.example.demo.model.Project;
import com.example.demo.model.TaskGroup;

import java.util.Set;
import java.util.stream.Collectors;

public class GroupWriteModel {

    private String description;
    private Set<GroupTaskWriteModel> tasks;

    public TaskGroup toGroup(Project project) {
        TaskGroup result = new TaskGroup();
        result.setDescription(this.description);
        result.setTasks(this.tasks.stream()
                .map(source -> source.toTask(result))
                .collect(Collectors.toSet()));
        result.setProject(project);
        return result;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<GroupTaskWriteModel> getTasks() {
        return tasks;
    }

    public void setTasks(Set<GroupTaskWriteModel> tasks) {
        this.tasks = tasks;
    }
}
