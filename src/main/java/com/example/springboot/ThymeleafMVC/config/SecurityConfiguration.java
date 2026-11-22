package com.example.springboot.ThymeleafMVC.config;

import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.Assert;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain directorySecurity(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(requests -> requests
                        .dispatcherTypeMatchers(DispatcherType.ERROR, DispatcherType.FORWARD).permitAll()
                        .anyRequest().hasRole("ADMIN"))
                .formLogin(login -> login.defaultSuccessUrl("/employees/list").permitAll())
                .logout(withDefaults())
                .build();
    }

    @Bean
    UserDetailsService directoryUsers(@Value("${DIRECTORY_ADMIN_USERNAME:admin}") String username,
                                     @Value("${DIRECTORY_ADMIN_PASSWORD}") String password) {
        Assert.hasText(password, "DIRECTORY_ADMIN_PASSWORD must be set");
        return new InMemoryUserDetailsManager(User.withUsername(username)
                .password(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(password))
                .roles("ADMIN")
                .build());
    }
}
