package com.example.demo.adapter;

import com.example.demo.model.Project;
import com.example.demo.model.ProjectRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SqlProjectRepository extends JpaRepository<Project, Integer>, ProjectRepository {

    @Override
    @Query("SELECT DISTINCT p FROM Project p JOIN FETCH p.projectSteps")
    List<Project> findAll();
}
