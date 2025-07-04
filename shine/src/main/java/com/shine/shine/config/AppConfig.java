package com.shine.shine.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    // This bean makes the password encoder available everywhere in your application.
    // It's the standard, secure way to hash passwords.
    /*@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }*/
}