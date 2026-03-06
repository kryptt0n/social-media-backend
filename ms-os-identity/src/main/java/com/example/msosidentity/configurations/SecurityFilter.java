package com.example.msosidentity.configurations;

import com.example.msosidentity.dto.UserMeDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class SecurityFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String username = request.getHeader("X-Username");
        String userIdString = request.getHeader("X-User-Id");

        if (request.getRequestURI().startsWith("/identity/me")) {
            filterChain.doFilter(request, response);
            return;
        }


        if (username == null || username.isBlank() || userIdString == null || userIdString.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        Integer userId;

        try {
            userId = Integer.valueOf(userIdString);
        } catch (NumberFormatException e) {
            userId = null;
        }

        SecurityContext securityContext = SecurityContextHolder.getContext();
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(new UserMeDTO(username, userId), null, List.of());
        securityContext.setAuthentication(auth);

        filterChain.doFilter(request, response);

    }
}
