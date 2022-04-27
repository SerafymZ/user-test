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

//    @Autowired
//    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication().dataSource(dataSource);
        auth.inMemoryAuthentication()
                .withUser("user").password("{bcrypt}$2a$10$j5DUDV4o2reodruq9086FePFB4fxLrzq/0ZUqh3zXotYEaC2oemai").roles("USER")
                .and()
                .withUser("admin").password("{bcrypt}$2a$10$50Oag0ifCFghZ1pMU5WeSO1hKHfpgY2DHBAb2TUv/vgK7SWy81IqS").roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/user").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/user/{id:\\d+}").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/user").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/user/{id:\\d+}").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/user/{id:\\d+}").hasRole("ADMIN")
                .and()
                .csrf().disable()
                .formLogin().disable();
    }
}
