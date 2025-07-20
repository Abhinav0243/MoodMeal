package com.example.MoodMeal.controller;

import com.example.MoodMeal.dto.LoginRequest;
import com.example.MoodMeal.security.JWTUtil;
import com.example.MoodMeal.model.User;
import com.example.MoodMeal.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private com.example.MoodMeal.service.CustomerDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(), loginRequest.getPassword()
                    )
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
            String token = jwtUtil.generateToken(userDetails);

            User user = userRepository.findByUsername(loginRequest.getUsername()).orElse(null);

            return ResponseEntity.ok(
                    new LoginResponse(token, user.getId(), user.getUsername(), user.getRoles())
            );

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    // Inner DTO for response
    static class LoginResponse {
        private String token;
        private Long userId;
        private String username;
        private Object roles;

        public LoginResponse(String token, Long userId, String username, Object roles) {
            this.token = token;
            this.userId = userId;
            this.username = username;
            this.roles = roles;
        }

        public String getToken() { return token; }
        public Long getUserId() { return userId; }
        public String getUsername() { return username; }
        public Object getRoles() { return roles; }
    }
}
