package com.example.demo.logic;

import com.example.demo.model.*;
import com.example.demo.model.projection.GroupReadModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskGroupRepository taskGroupRepository;
    private final TaskConfigurationProperties configurationProperties;

    public ProjectService(ProjectRepository projectRepository, TaskGroupRepository taskGroupRepository, TaskConfigurationProperties configurationProperties) {
        this.projectRepository = projectRepository;
        this.taskGroupRepository = taskGroupRepository;
        this.configurationProperties = configurationProperties;
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Project save(Project project) {
        return projectRepository.save(project);
    }

    public GroupReadModel createGroup(LocalDateTime deadline, Integer projectId) {
        if (!configurationProperties.getTemplate().isAllowMultipleTasks() && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group form project is allowed");
        }

        TaskGroup result = projectRepository.findById(projectId)
                .map(project -> {
                    TaskGroup taskGroup = new TaskGroup();
                    taskGroup.setDescription(project.getDescription());
                    taskGroup.setTasks(project.getProjectSteps().stream()
                            .map(step -> Task.createNewTask(step.getDescription(), deadline.plusDays(step.getDaysToDeadline())))
                            .collect(Collectors.toSet()));
                    taskGroup.setProject(project);
                    return taskGroupRepository.save(taskGroup);
                }).orElseThrow(() -> new NoSuchElementException(String.format("No project with ID %d found", projectId)));

        return new GroupReadModel(result);
    }

}