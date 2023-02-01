package com.example.demo.model.projection;

import com.example.demo.model.TaskGroup;

import java.util.Set;
import java.util.stream.Collectors;

public class GroupWriteModel {

    private String description;
    private Set<GroupTaskWriteModel> tasks;

    public TaskGroup toGroup() {
        return TaskGroup.CreateNewTaskGroup(
                this.description,
                this.tasks.stream()
                        .map(GroupTaskWriteModel::toTask)
                        .collect(Collectors.toSet()));
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
