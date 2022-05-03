package com.usertest.controller.initializer;

import lombok.experimental.UtilityClass;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.MSSQLServerContainer;

@UtilityClass
public class MsSQLServer {

    public static MSSQLServerContainer<?> container =
            new MSSQLServerContainer<>("mcr.microsoft.com/mssql/server:2019-latest").acceptLicense();

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {


        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + container.getJdbcUrl(),
                    "spring.datasource.username=" + container.getUsername(),
                    "spring.datasource.password=" + container.getPassword()
            ).applyTo(applicationContext);
        }
    }
}
