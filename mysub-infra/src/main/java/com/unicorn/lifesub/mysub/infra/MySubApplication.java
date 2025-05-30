package com.unicorn.lifesub.mysub.infra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(
        scanBasePackages = {
                "com.unicorn.lifesub.mysub",
                "com.unicorn.lifesub.common"
        }
)
public class MySubApplication {
    public static void main(String[] args) {
        SpringApplication.run(MySubApplication.class, args);
    }
}
