package com.example.demo.aspect;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class LogicAspect {
    private final Timer projectCreateGroupTimer;

    public LogicAspect(final MeterRegistry registry) {
        this.projectCreateGroupTimer = registry.timer("logic.project.create.group");
    }

    @Around("execution(* com.example.demo.logic.ProjectService.createGroup(..))")
    Object aroundProjectCreateGroup(ProceedingJoinPoint jp) {
        return projectCreateGroupTimer.record(() -> {
            try {
                return jp.proceed();
            } catch (Throwable e) {
                if (e instanceof RuntimeException) throw (RuntimeException) e;
                else throw new RuntimeException(e);
            }
        });
    }
}
