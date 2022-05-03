package com.usertest.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_USER = "USER";

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/user").hasAnyRole(ROLE_USER, ROLE_ADMIN)
                .antMatchers(HttpMethod.GET, "/user/{id:\\d+}").hasRole(ROLE_ADMIN)
                .antMatchers(HttpMethod.POST, "/user").hasRole(ROLE_ADMIN)
                .antMatchers(HttpMethod.PUT, "/user/{id:\\d+}").hasRole(ROLE_ADMIN)
                .antMatchers(HttpMethod.DELETE, "/user/{id:\\d+}").hasRole(ROLE_ADMIN)
                .anyRequest().denyAll()
                .and()
                .csrf().disable()
                .formLogin().disable();
    }
}
