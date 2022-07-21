package com.mineaurion.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private TokenFilter tokenFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .exceptionHandling()
                .and()
                .csrf().disable()
                .addFilterAfter(tokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .mvcMatchers(
                        HttpMethod.GET,
                        "/swagger-doc/**", "/swagger-ui/**", "/v1/**", "/query/**", "/actuator/**"
                ).permitAll()
                // .antMatchers("/h2-console/**", "/h2-console").permitAll()
                .anyRequest().authenticated()
        ;

        return http.build();
    }
}