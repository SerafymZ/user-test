package com.usertest.controller.initializer;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.MSSQLServerContainer;

import javax.sql.DataSource;

@UtilityClass
public class MsSQLServer {

    public static MSSQLServerContainer<?> container =
            new MSSQLServerContainer<>("mcr.microsoft.com/mssql/server:2019-latest").acceptLicense();

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {


        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();

            var hikariConfig = new HikariConfig();
            hikariConfig.setUsername(container.getUsername());
            hikariConfig.setPassword(container.getPassword());
            hikariConfig.setJdbcUrl(container.getJdbcUrl());

            DataSource dataSource = new HikariDataSource(hikariConfig);
            beanFactory.registerSingleton(dataSource.getClass().getCanonicalName(), dataSource);
        }
    }
}
