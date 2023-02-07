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

import java.time.LocalDateTime;
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
    void clean() {
        testEntityManager.clear();
    }

    @Test
    void existsByDoneIsFalseAndTaskGroup_Id_should_return_tasks_by_given_group_id() {
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

    @Test
    void findAllByDoneIsFalseAndDeadlineLessThanEqual_should_return_undone_tasks_with_deadline_before_or_today_sorted_by_deadline() {
        //given
        Task undone_null = Task.createNewTask("null", null);
        Task null_but_done = Task.createNewTask("null but done", null);
        null_but_done.setDone(true);
        Task undone_today1 = Task.createNewTask("undone1", LocalDateTime.now());
        Task undone_today2 = Task.createNewTask("undone2", LocalDateTime.now());
        Task undone_yesterday1 = Task.createNewTask("undone3 yesterday", LocalDateTime.now().minusDays(1));
        Task undone_week_back_1 = Task.createNewTask("undone3 week back", LocalDateTime.now().minusDays(7));
        Task done_today1 = Task.createNewTask("done", LocalDateTime.now());
        done_today1.setDone(true);
        Task undone_tomorrow = Task.createNewTask("undone, but for tomorrow", LocalDateTime.now().plusDays(1));
        testEntityManager.persist(undone_null);
        testEntityManager.persist(null_but_done);
        testEntityManager.persist(undone_today1);
        testEntityManager.persist(undone_today2);
        testEntityManager.persist(undone_yesterday1);
        testEntityManager.persist(undone_week_back_1);
        testEntityManager.persist(done_today1);
        testEntityManager.persist(undone_tomorrow);

        //when
        List<Task> result = sqlTaskRepository.findAllByDoneIsFalseAndDeadlineLessThanEqualOrDeadlineIsNullAndDoneIsFalseOrderByDeadlineAsc(LocalDateTime.now());

        //then
        assertEquals(List.of(undone_null, undone_week_back_1, undone_yesterday1, undone_today1, undone_today2), result);
    }

    @Test
    void findAllTasksForToday_should_return_undone_tasks_with_deadline_before_or_today_sorted_by_deadline() {
        //given
        Task undone_null = Task.createNewTask("null", null);
        Task null_but_done = Task.createNewTask("null but done", null);
        null_but_done.setDone(true);
        Task undone_today1 = Task.createNewTask("undone1", LocalDateTime.now());
        Task undone_today2 = Task.createNewTask("undone2", LocalDateTime.now());
        Task undone_yesterday1 = Task.createNewTask("undone3 yesterday", LocalDateTime.now().minusDays(1));
        Task undone_week_back_1 = Task.createNewTask("undone3 week back", LocalDateTime.now().minusDays(7));
        Task done_today1 = Task.createNewTask("done", LocalDateTime.now());
        done_today1.setDone(true);
        Task undone_tomorrow = Task.createNewTask("undone, but for tomorrow", LocalDateTime.now().plusDays(1));
        testEntityManager.persist(undone_null);
        testEntityManager.persist(null_but_done);
        testEntityManager.persist(undone_today1);
        testEntityManager.persist(undone_today2);
        testEntityManager.persist(undone_yesterday1);
        testEntityManager.persist(undone_week_back_1);
        testEntityManager.persist(done_today1);
        testEntityManager.persist(undone_tomorrow);

        //when
        List<Task> result = sqlTaskRepository.findAllTasksForToday(LocalDateTime.now());

        //then
        assertEquals(List.of(undone_null, undone_week_back_1, undone_yesterday1, undone_today1, undone_today2), result);
    }

}