package com.example.demo.logic;

import com.example.demo.model.TaskConfigurationProperties;
import com.example.demo.model.TaskGroup;
import com.example.demo.model.TaskGroupRepository;
import com.example.demo.model.TaskRepository;
import com.example.demo.model.projection.GroupReadModel;
import com.example.demo.model.projection.GroupWriteModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class TaskGroupService {

    private final TaskGroupRepository repository;
    private final TaskRepository taskRepository;

    public TaskGroupService(TaskGroupRepository repository, TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel createGroup(GroupWriteModel source) {
        TaskGroup result = repository.save(source.toGroup());
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll() {
        return repository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(Integer groupId) {
        if (taskRepository.existsByDoneIsFalseAndTaskGroup_Id(groupId)){
            throw new IllegalStateException("Group has undone tasks.");
        }
        TaskGroup taskGroup = repository.findById(groupId)
                .orElseThrow(() -> new NoSuchElementException(String.format("No group with %d ID found.", groupId)));

        taskGroup.setDone(!taskGroup.isDone());
    }
}
