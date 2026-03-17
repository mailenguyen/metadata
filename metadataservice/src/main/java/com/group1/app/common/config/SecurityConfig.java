package com.group1.app.common.config;

import com.group1.app.common.security.HeaderAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
// 🔒 ENABLE LẠI KHI CẦN AUTH METHOD LEVEL
// @EnableMethodSecurity
public class SecurityConfig {

    private final HeaderAuthenticationFilter headerAuthenticationFilter;

    public SecurityConfig(HeaderAuthenticationFilter headerAuthenticationFilter) {
        this.headerAuthenticationFilter = headerAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // ❌ Disable CSRF (ok cho API stateless)
                .csrf(csrf -> csrf.disable())

                // ❌ Không dùng session
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // ✅ DEBUG MODE: cho phép toàn bộ request
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(
                                        "/v3/api-docs/**",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html"
                                ).permitAll()

                                // 🔥 DEBUG: mở toàn bộ để test gateway/eureka
                                .anyRequest().permitAll()

                /*
                 🔒 PRODUCTION (bật lại khi cần)
                 .anyRequest().authenticated()
                 */
                );

        // ❌ DEBUG: TẮT filter để tránh 403
        // 👉 Đây là nguyên nhân chính gây 403 hiện tại
        /*
        http.addFilterBefore(
            headerAuthenticationFilter,
            UsernamePasswordAuthenticationFilter.class
        );
        */

        return http.build();
    }
}