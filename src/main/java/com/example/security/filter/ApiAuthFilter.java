package com.example.security.filter;

import com.example.exception.HttpError;
import com.example.security.services.TokenServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ApiAuthFilter extends OncePerRequestFilter {

    private final TokenServices jwtTokenServices;

    @Autowired
    public ApiAuthFilter(TokenServices jwtTokenServices) {
        this.jwtTokenServices = jwtTokenServices;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenServices.getAccessToken(req);

        try {
            if (token != null && jwtTokenServices.isValidToken(token)) {
                Authentication auth = jwtTokenServices.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (HttpError e) {
            SecurityContextHolder.clearContext();
            resp.sendError(e.getHttpStatus().value(), e.getMessage());
            return;
        }

        filterChain.doFilter(req, resp);
    }
}