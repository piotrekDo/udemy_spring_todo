package com.example.demo.logic;

import com.example.demo.model.Task;
import com.example.demo.model.TaskGroup;
import com.example.demo.model.TaskGroupRepository;
import com.example.demo.model.TaskRepository;
import com.example.demo.model.projection.GroupReadModel;
import com.example.demo.model.projection.GroupWriteModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class TaskGroupService {

    private final TaskGroupRepository taskGroupRepository;
    private final TaskRepository taskRepository;

    public TaskGroupService(TaskGroupRepository repository, TaskRepository taskRepository) {
        this.taskGroupRepository = repository;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel createGroup(GroupWriteModel source) {
        TaskGroup result = taskGroupRepository.save(source.toGroup());
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll() {
        return taskGroupRepository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void toggleGroup(Integer groupId) {
        if (taskRepository.existsByDoneIsFalseAndTaskGroup_Id(groupId)){
            throw new IllegalStateException("Group has undone tasks.");
        }
        TaskGroup taskGroup = taskGroupRepository.findById(groupId)
                .orElseThrow(() -> new NoSuchElementException(String.format("No group with %d ID found.", groupId)));

        taskGroup.setDone(!taskGroup.isDone());
    }

    public List<Task> getTasksByGroup(int groupId) {
        return taskRepository.findAllByTaskGroup_Id(groupId);
    }
}
