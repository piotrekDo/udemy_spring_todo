package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * Zapis z wykorzystniem klas Spriinga odpowiedzialnych za poszczególne propsy.
     */

    private final DataSourceProperties dataSourceProperties;

    @Value("${my.prop}")
    private String prop;

    public InfoController(DataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    @GetMapping("/url")
    String url() {
        return dataSourceProperties.getUrl();
    }

    @GetMapping("/prop")
    String myProp() {
        return prop;
    }

}
