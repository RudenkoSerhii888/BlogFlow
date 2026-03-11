package org.example.blog.config;

import org.example.blog.model.UserRole;
import org.example.blog.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

    public static final String ADMIN = "admin";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner initData(UserService userService,
                                      PasswordEncoder encoder) {
        return args -> {
            userService.addUser(ADMIN,
                    encoder.encode("password"),
                    UserRole.ADMIN, "", "", "");
            userService.addUser("user",
                    encoder.encode("password"),
                    UserRole.USER, "", "", "");
        };
    }
}