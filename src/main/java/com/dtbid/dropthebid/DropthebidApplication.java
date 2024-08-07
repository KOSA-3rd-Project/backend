package com.dtbid.dropthebid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DropthebidApplication {

    public static void main(String[] args) {
        SpringApplication.run(DropthebidApplication.class, args);
    }

}
