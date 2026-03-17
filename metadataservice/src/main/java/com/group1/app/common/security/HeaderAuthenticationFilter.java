package com.group1.app.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

@Component
public class HeaderAuthenticationFilter extends OncePerRequestFilter {

    private static final String HEADER_USER_ID = "X-User-Id";
    private static final String HEADER_USER_ROLE = "X-User-Role";
    private static final String HEADER_USER_NAME = "X-User-Name";
    private static final String HEADER_USER_PERMISSIONS = "X-User-Permissions";

    private static final Logger log = LoggerFactory.getLogger(HeaderAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String userId = request.getHeader(HEADER_USER_ID);
        String role = request.getHeader(HEADER_USER_ROLE);
        String name = request.getHeader(HEADER_USER_NAME);

        log.debug("Incoming headers: {}={}, {}={}, {}={}, AuthorizationPresent={}",
                HEADER_USER_ID, userId,
                HEADER_USER_ROLE, role,
                HEADER_USER_NAME, name,
                request.getHeader("Authorization") != null || request.getHeader("authorization") != null
        );

        if (userId != null && role != null) {

            List<SimpleGrantedAuthority> authorities = new ArrayList<>();

            String roleAuthority = role.startsWith("ROLE_") ? role : "ROLE_" + role;
            authorities.add(new SimpleGrantedAuthority(roleAuthority));

            String permissionsHeader = request.getHeader(HEADER_USER_PERMISSIONS);
            if (permissionsHeader != null && !permissionsHeader.isBlank()) {
                String[] permissions = permissionsHeader.split(",");
                for (String permission : permissions) {
                    String trimmed = permission == null ? "" : permission.trim();
                    if (!trimmed.isEmpty()) {
                        authorities.add(new SimpleGrantedAuthority(trimmed));
                    }
                }
            }

            UserPrincipal principal = new UserPrincipal(userId, name);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            principal,
                            null,
                            authorities
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Set authentication for userId={}", userId);
        }

        filterChain.doFilter(request, response);
    }
}