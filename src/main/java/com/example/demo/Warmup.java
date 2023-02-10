package com.example.demo;

import com.example.demo.model.Task;
import com.example.demo.model.TaskGroup;
import com.example.demo.model.TaskGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Set;


@Component
public class Warmup implements ApplicationListener<ContextRefreshedEvent> {
    private final static Logger logger = LoggerFactory.getLogger(Warmup.class);
    private final TaskGroupRepository taskGroupRepository;

    public Warmup(TaskGroupRepository taskGroupRepository) {
        this.taskGroupRepository = taskGroupRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("Application warmup after context refreshed");
        String description = "ApplicationContextEvent";
        if (!taskGroupRepository.existsByDescription(description)){
            logger.warn("No required group found, adding it now");
            TaskGroup taskGroup = new TaskGroup();
            taskGroup.setDescription(description);
            taskGroup.setTasks(Set.of(
                    new Task("ContextClosedEvent", null, taskGroup),
                    new Task("ContextRefreshedEvent", null, taskGroup),
                    new Task("ContextStoppedEvent", null, taskGroup),
                    new Task("ContextStartedEvent", null, taskGroup)
            ));
            taskGroupRepository.save(taskGroup);
        }
    }
}
