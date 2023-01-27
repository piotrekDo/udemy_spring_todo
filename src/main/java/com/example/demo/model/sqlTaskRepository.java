package com.example.demo.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
interface sqlTaskRepository extends TaskRepository, JpaRepository<Task, Integer> {
}
