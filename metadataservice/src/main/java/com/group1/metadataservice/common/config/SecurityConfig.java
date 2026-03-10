package com.group1.metadataservice.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(basic -> {});

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // For development/testing only. Replace with BCrypt in production.
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public UserDetailsService users(
            @Value("${spring.security.user.name:admin}") String adminName,
            @Value("${spring.security.user.password:admin123}") String adminPass,
            @Value("${spring.security.user.roles:ADMIN}") String adminRoles,
            @Value("${spring.security1.user.name:}") String managerName,
            @Value("${spring.security1.user.password:}") String managerPass,
            @Value("${spring.security1.user.roles:MANAGER}") String managerRoles
    ) {
        List<org.springframework.security.core.userdetails.UserDetails> list = new ArrayList<>();

        list.add(User.withUsername(adminName)
                .password(adminPass)
                .roles(adminRoles.split(","))
                .build());

        if (managerName != null && !managerName.isBlank()) {
            list.add(User.withUsername(managerName)
                    .password(managerPass)
                    .roles(managerRoles.split(","))
                    .build());
        }

        return new InMemoryUserDetailsManager(list);
    }
}