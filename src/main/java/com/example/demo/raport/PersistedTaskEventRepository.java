package com.example.demo.raport;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersistedTaskEventRepository extends JpaRepository<PersistedTaskEvent, Integer> {
    List<PersistedTaskEvent> findAllByTaskId(int task_id);
}
