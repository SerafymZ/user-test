package com.usertest.config;

import com.usertest.logging.LoggingDispatcherServlet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
public class DispatcherConfig {

    @Bean
    DispatcherServlet dispatcherServlet() {
        return new LoggingDispatcherServlet();
    }
}
