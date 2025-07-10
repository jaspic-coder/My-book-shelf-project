package org.example.mybooklibrary.config;

import org.example.mybooklibrary.security.JwtAuthenticationEntryPoint;  // <-- import your new entry point
import org.example.mybooklibrary.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;  // Add this

    public SecurityConfig(JwtUtil jwtUtil, JwtAuthenticationEntryPoint authenticationEntryPoint) {
        this.jwtUtil = jwtUtil;
        this.authenticationEntryPoint = authenticationEntryPoint;  // Initialize here
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Allow GET requests for books and reset-password URLs without auth
                        .requestMatchers(HttpMethod.GET, "/api/books/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/auth/reset-password/**").permitAll()
                        // Allow public access to these endpoints
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login",
                                "/api/auth/otp/**",
                                "/api/auth/forgot-password",
                                "/api/auth/reset-password/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/api/images/upload"
                        ).permitAll()
                        // Require auth for any other requests
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint))  // Add this for handling 401 errors
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}