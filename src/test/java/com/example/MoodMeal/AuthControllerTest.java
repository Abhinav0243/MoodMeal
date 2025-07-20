package com.example.MoodMeal;

import com.example.MoodMeal.controller.AuthController;
import com.example.MoodMeal.dto.LoginRequest;
import com.example.MoodMeal.model.Role;
import com.example.MoodMeal.model.User;
import com.example.MoodMeal.repository.UserRepository;
import com.example.MoodMeal.security.JWTUtil;
import com.example.MoodMeal.service.CustomerDetailsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.BadCredentialsException;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Set;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private JWTUtil jwtUtil;

    @MockitoBean
    private CustomerDetailsService userDetailsService;

    @MockitoBean
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Should return token and user info on successful login")
    void testLogin_success() throws Exception {
        String username = "testuser";
        String password = "password";
        Long userId = 1L;
        String token = "dummy.jwt.token";
        Set<Role> roles = Set.of(Role.ROLE_USER);

        // Compose request
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        // Mock authentication manager DOES NOT throw
        Authentication mockAuth = new UsernamePasswordAuthenticationToken(username, password);
        Mockito.when(authenticationManager.authenticate(any())).thenReturn(mockAuth);

        // Mock loadUserByUsername
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password("encoded") // password won't be checked
                .authorities("ROLE_USER")
                .build();
        Mockito.when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        // Mock JWT generation
        Mockito.when(jwtUtil.generateToken(userDetails)).thenReturn(token);

        // Mock DB user lookup
        User user = new User();
        user.setId(userId);
        user.setUsername(username);
        user.setRoles(roles);
        Mockito.when(userRepository.findByUsername(eq(username))).thenReturn(Optional.of(user));

        // Fire request
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token))
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"));
    }

    @Test
    @DisplayName("Should return 401 for invalid login")
    void testLogin_failure() throws Exception {
        String username = "baduser";
        String password = "badpass";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        // AuthenticationManager throws
        Mockito.doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager).authenticate(any());

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid username or password"));
    }
}
