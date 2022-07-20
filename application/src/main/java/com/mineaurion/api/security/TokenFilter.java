package com.mineaurion.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Optional;

@Component
public class TokenFilter extends OncePerRequestFilter {

    private final String HEADER = "Authorization";
    private final String PREFIX = "Bearer ";

    @Autowired
    private Environment env;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String configToken = env.getProperty("auth.token");
        Optional<String> tokenHeader = Optional.ofNullable(request.getHeader(HEADER));
        if (tokenHeader.isPresent()) {
            String token = tokenHeader.get().replace(PREFIX, "");
            if (token.equals(configToken)) {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("token", null, null);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                throw new AccessDeniedException("Forbidden Token");
            }
        } else {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);

    }
}
