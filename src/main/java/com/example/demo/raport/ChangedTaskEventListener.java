package com.example.demo.raport;

import com.example.demo.model.event.TaskDone;
import com.example.demo.model.event.TaskUndone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class ChangedTaskEventListener {

    private static final Logger logger = LoggerFactory.getLogger(ChangedTaskEventListener.class);

    private final PersistedTaskEventRepository repository;

    public ChangedTaskEventListener(PersistedTaskEventRepository repository) {
        this.repository = repository;
    }

    @EventListener
    public void on(TaskDone event) {
        logger.info("got: " + event);
        repository.save(new PersistedTaskEvent(event));
    }

    @EventListener
    public void on(TaskUndone event){
        logger.info("got " + event);
        repository.save(new PersistedTaskEvent(event));
    }
}
