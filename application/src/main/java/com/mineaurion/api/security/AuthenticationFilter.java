package com.mineaurion.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Optional;

@Component
public class AuthenticationFilter extends GenericFilterBean {

    private final String HEADER = "Authorization";

    private final String PREFIX = "Bearer ";

    @Autowired
    private Environment env;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String configToken = env.getProperty("auth.token");
        Optional<String> tokenHeader = Optional.ofNullable(((HttpServletRequest)request).getHeader(HEADER));
        if(tokenHeader.isPresent()){
            String token = tokenHeader.get().replace(PREFIX, "");
            if(token.equals(configToken)){
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("token", null, null);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }
}
