package com.nishant.assignment.springassignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class SpringassignmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringassignmentApplication.class, args);
    }

}
