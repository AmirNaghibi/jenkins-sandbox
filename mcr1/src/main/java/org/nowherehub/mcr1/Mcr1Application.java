package org.nowherehub.mcr1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
@Import(Mcr1SecurityConfig.class)
public class Mcr1Application {

    public static void main(String[] args) {
        SpringApplication.run(Mcr1Application.class, args);
    }
}
