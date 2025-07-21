package com.example.MoodMeal.security;

import com.example.MoodMeal.service.CustomerDetailsService;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JWTAuthFilter jwtAuthFilter;
    private final CustomerDetailsService customerDetailsService;

    @Autowired
    public SecurityConfig(JWTAuthFilter jwtAuthFilter, CustomerDetailsService customerDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.customerDetailsService = customerDetailsService;
    }

    // ➤ Password encoder using BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ➤ Expose your custom UserDetailsService as a Bean
    @Bean
    public UserDetailsService userDetailsService() {
        return customerDetailsService;
    }

    // ➤ DaoAuthenticationProvider for authentication logic
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customerDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // ➤ AuthenticationManager bean (used in AuthController)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // ➤ Main Spring Security filter chain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for API
                .cors(cors -> {})             // Allow CORS (custom config can be added if needed)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless session
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/register", "/api/users/login").permitAll()
                        .requestMatchers("/api/meals/**", "/api/moods/**", "/api/suggestions/**").authenticated()
                        .anyRequest().permitAll()
                )
                .authenticationProvider(authenticationProvider()) // Register your provider
                .addFilterBefore((Filter) jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // JWT before default filter

        return http.build();
    }
}
