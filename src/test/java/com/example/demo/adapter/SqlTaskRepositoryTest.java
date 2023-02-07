package com.example.demo.adapter;

import com.example.demo.model.Task;
import com.example.demo.model.TaskGroup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class SqlTaskRepositoryTest {

    @Autowired
    SqlTaskRepository sqlTaskRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @AfterEach
    void clean(){
        testEntityManager.clear();
    }

    @Test
    void existsByDoneIsFalseAndTaskGroup_Id_should_return_tasks_by_given_group_id(){
        //given
        TaskGroup group1 = TaskGroup.CreateNewTaskGroup("group1", new HashSet<>());
        TaskGroup group2 = TaskGroup.CreateNewTaskGroup("group2", new HashSet<>());
        Task task1 = Task.createNewTask("task1", null);
        Task task2 = Task.createNewTask("task2", null);
        Task task3 = Task.createNewTask("task3", null);
        task1.setTaskGroup(group1);
        task2.setTaskGroup(group1);
        task3.setTaskGroup(group2);
        group1.getTasks().add(task1);
        group1.getTasks().add(task2);
        testEntityManager.persist(task1);
        testEntityManager.persist(task2);
        testEntityManager.persist(task3);
        TaskGroup savedGroup1 = testEntityManager.persist(group1);
        testEntityManager.persist(group2);

        //when
        List<Task> result = sqlTaskRepository.findAllByTaskGroup_Id(savedGroup1.getId());

        //then
        assertIterableEquals(List.of(task1, task2), result);
        assertNotEquals(task3.getTaskGroup().getId(), savedGroup1.getId());
    }

}