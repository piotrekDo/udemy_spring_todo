package com.example.demo.logic;

import com.example.demo.model.*;
import com.example.demo.model.projection.GroupReadModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;

    @MockBean
    private ProjectRepository projectRepository;
    @MockBean
    private TaskGroupRepository taskGroupRepository;
    @MockBean
    private TaskConfigurationProperties configurationProperties;
    @Mock
    private TaskConfigurationProperties.Template template;

    @TestConfiguration
    static class ProjectServiceTestConfig {
        @Bean
        ProjectService projectService(ProjectRepository projectRepository, TaskGroupRepository taskGroupRepository, TaskConfigurationProperties configurationProperties ){
            return new ProjectService(projectRepository, taskGroupRepository, configurationProperties);
        }
    }


    @Test
    void should_return_new_group_read_model() {
        //given
        LocalDateTime deadline = LocalDateTime.now();
        Integer projectId = 99;
        Project projectById = new Project(projectId, "test project");
        projectById.setProjectSteps(Set.of(new ProjectStep("test1", 2)));
        TaskGroup taskGroupSaved = TaskGroup.CreateNewTaskGroup(projectById.getDescription(), Set.of(Task.createNewTask("test1", LocalDateTime.now())));
        GroupReadModel expectedResult = new GroupReadModel(taskGroupSaved);
        expectedResult.setDeadline(expectedResult.getDeadline().plusDays(2));
        Mockito.when(configurationProperties.getTemplate()).thenReturn(template);
        Mockito.when(template.isAllowMultipleTasks()).thenReturn(true);
        Mockito.when(taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)).thenReturn(false);
        Mockito.when(projectRepository.findById(projectId)).thenReturn(Optional.of(projectById));

        //when
        GroupReadModel result = projectService.createGroup(deadline, projectId);

        //then
        assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("should throw IllegalStateException when configured to allow just 1 group and the other undone group exists")
    void createGroup_noMultipleGroupsConfig_And_openGroups_throws_an_exception() {
        //given
        Mockito.when(configurationProperties.getTemplate()).thenReturn(template);
        Mockito.when(template.isAllowMultipleTasks()).thenReturn(false);
        Mockito.when(taskGroupRepository.existsByDoneIsFalseAndProject_Id(Mockito.anyInt())).thenReturn(true);

        //when && then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> projectService.createGroup(LocalDateTime.now(), 1));
        assertEquals("Only one undone group form project is allowed", exception.getMessage());
        Mockito.verify(projectRepository, Mockito.never()).findById(Mockito.anyInt());
    }
}