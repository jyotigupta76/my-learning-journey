package com.security.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity  //Enable security
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    DataSource dataSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http.authorizeHttpRequests(authorizeRequests ->
                authorizeRequests.requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated());
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }

    //UserDetailsService is used by Spring Security to load user details during authentication.
    // User details can come from a database, in-memory storage, or another custom source, depending on the application's requirements and implementation.

    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails user1 = User.withUsername("user1")
                .password("{noop}password1")
                .roles("USER")
                .build();

        UserDetails user2 = User.withUsername("user2")
                .password("{noop}password2")
                .roles("USER")
                .build();

        UserDetails admin = User.withUsername("admin1")
                .password("{noop}123456")
                .roles("USER")
                .build();

//        return new InMemoryUserDetailsManager(user1, user2, admin);

        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
        userDetailsManager.createUser(user1);
        userDetailsManager.createUser(user2);
        userDetailsManager.createUser(admin);
        return userDetailsManager;




    }

}
