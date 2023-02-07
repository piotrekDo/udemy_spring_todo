package com.example.demo.adapter;

import com.example.demo.model.Task;
import com.example.demo.model.TaskRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
interface SqlTaskRepository extends TaskRepository, JpaRepository<Task, Integer> {

    @Override
    @Query(nativeQuery = true, value = "SELECT COUNT(*) > 0 FROM tasks WHERE id=:id")
    boolean existsById(@Param("id") Integer id);

    @Override
    boolean existsByDoneIsFalseAndTaskGroup_Id(Integer taskGroupId);

    @Override
    List<Task> findAllByTaskGroup_Id(int id);

    @Override
    List<Task> findAllByDoneIsFalseAndDeadlineLessThanEqualOrDeadlineIsNullAndDoneIsFalseOrderByDeadlineAsc(LocalDateTime date);

    @Override
    @Query("SELECT t FROM Task t WHERE t.done = false AND (t.deadline IS NULL OR t.deadline <= ?1) ORDER BY t.deadline ASC")
    List<Task> findAllTasksForToday(LocalDateTime date);
}
