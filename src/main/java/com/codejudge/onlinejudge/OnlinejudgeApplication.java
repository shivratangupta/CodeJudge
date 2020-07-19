package com.codejudge.onlinejudge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class OnlinejudgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlinejudgeApplication.class, args);
    }

}
