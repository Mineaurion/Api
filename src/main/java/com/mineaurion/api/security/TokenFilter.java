package com.mineaurion.api.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class TokenFilter extends OncePerRequestFilter {

    private final String HEADER = "Authorization";
    private final String PREFIX = "Bearer ";

    private String configToken;

    public TokenFilter(String configToken){
        this.configToken = configToken;
    }

    public TokenFilter(){
        this("mineaurion");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> tokenHeader = Optional.ofNullable(request.getHeader(HEADER));
        if (tokenHeader.isPresent()) {
            String token = tokenHeader.get().replace(PREFIX, "");
            if (token.equals(configToken)) {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("token", null, null);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden token");
            }
        } else {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);

    }
}
