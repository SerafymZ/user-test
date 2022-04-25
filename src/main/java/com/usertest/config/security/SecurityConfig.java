package com.usertest.config.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        UserBuilder userBuilder = User.withDefaultPasswordEncoder();
        auth.inMemoryAuthentication().withUser(userBuilder.username("user").password("user").roles("USER"))
                .withUser(userBuilder.username("admin").password("admin").roles("ADMIN"));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET).hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.POST).hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT).hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE).hasRole("ADMIN")
                .and().formLogin().permitAll();
    }
}
