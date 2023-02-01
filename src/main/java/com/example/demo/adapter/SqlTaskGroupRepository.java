package com.example.demo.adapter;

import com.example.demo.model.TaskGroup;
import com.example.demo.model.TaskGroupRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SqlTaskGroupRepository extends JpaRepository<TaskGroup, Integer>, TaskGroupRepository {

    @Override
    @Query("FROM TaskGroup g JOIN FETCH g.tasks")
    List<TaskGroup> findAll();

}
