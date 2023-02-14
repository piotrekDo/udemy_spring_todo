package com.example.demo.controller;

import com.example.demo.model.TaskConfigurationProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/info")
public class InfoController {

/**
 * Zapis z wykorzystaniem adnotacji @Value i przekazywaniem propsów 'na sztywno'
 */
//    @Value("${spring.datasource.url}")
//    private String url;
//    @Value("${my.prop}")
//    private String prop;
//
//    @GetMapping("/url")
//    String url() {
//        return url;
//    }
//
//    @GetMapping("/prop")
//    String myProp() {
//        return prop;
//    }

    /**
     * Zapis z wykorzystniem klas Springa odpowiedzialnych za poszczególne propsy.
     */

    private final DataSourceProperties dataSourceProperties;
    private final TaskConfigurationProperties taskConfigurationProperties;

    public InfoController(DataSourceProperties dataSourceProperties, TaskConfigurationProperties taskConfigurationProperties) {
        this.dataSourceProperties = dataSourceProperties;
        this.taskConfigurationProperties = taskConfigurationProperties;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/url")
    String url() {
        return dataSourceProperties.getUrl();
    }

    @RolesAllowed({"ROLES_ADMIN"})
    @GetMapping("/prop")
    boolean myProp() {
        return taskConfigurationProperties.getTemplate().isAllowMultipleTasks();
    }

}
