package com.example.MoodMeal;

import com.example.MoodMeal.controller.UserController;
import com.example.MoodMeal.model.User;
import com.example.MoodMeal.security.JWTAuthFilter;
import com.example.MoodMeal.service.CustomerDetailsService;
import com.example.MoodMeal.service.UserService;
import com.example.MoodMeal.security.JWTUtil;
import com.example.MoodMeal.exception.InvalidInputException;
import com.example.MoodMeal.exception.ResourceNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Set;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    // Only needed if your controller directly uses JWTUtil
    @MockitoBean
    private JWTUtil jwtUtil;

    @MockitoBean
    private JWTAuthFilter jwtAuthFilter;

    @MockitoBean // ADD THIS LINE
    private CustomerDetailsService customerDetailsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Should register a user successfully")
    void testRegisterUser_success() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setRoles(Set.of());

        Mockito.when(userService.registerUser(any(User.class))).thenReturn(user);

        String reqBody = """
            {
                "username": "testuser",
                "email": "test@example.com",
                "password": "password",
                "fullName": "Test User",
                "phone": 1234567890
            }
        """;

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqBody)
                        .with(csrf())) // <-- add this
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

    }

    @Test
    @DisplayName("Should handle duplicate username/email on registration")
    void testRegisterUser_duplicate() throws Exception {
        String reqBody = """
            {
                "username": "duplicate",
                "email": "exists@example.com",
                "password": "password"
            }
        """;

        Mockito.when(userService.registerUser(any(User.class)))
                .thenThrow(new InvalidInputException("Error: Username already taken"));

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Should get user by username")
    void testGetUserByUsername_success() throws Exception {
        User user = new User();
        user.setId(2L);
        user.setUsername("johnny");
        user.setEmail("johnny@example.com");

        Mockito.when(userService.getUserByUsername("johnny")).thenReturn(user);

        mockMvc.perform(get("/api/users/username/johnny"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("johnny"))
                .andExpect(jsonPath("$.email").value("johnny@example.com"));
    }

    @Test
    @DisplayName("Should return 404 when user not found by username")
    void testGetUserByUsername_notFound() throws Exception {
        Mockito.when(userService.getUserByUsername("missing"))
                .thenThrow(new ResourceNotFoundException("User not found"));

        mockMvc.perform(get("/api/users/username/missing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Should update user successfully")
    void testUpdateUser_success() throws Exception {
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("testuser");
        updatedUser.setEmail("test@example.com");
        updatedUser.setFullName("Test User");
        updatedUser.setPhone(1234567890L);
        updatedUser.setAllergies("None");
        updatedUser.setDietaryPerferences("Vegan");

        Mockito.when(userService.updateUser(eq(1L), any(User.class))).thenReturn(updatedUser);

        String reqBody = """
            {
                "fullName": "Test User",
                "phone": "123456789",
                "allergies": "None",
                "dietaryPerferences": "Vegan"
            }
        """;

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Test User"))
                .andExpect(jsonPath("$.dietaryPerferences").value("Vegan"));
    }

    @Test
    @DisplayName("Should change password successfully")
    void testChangePassword_success() throws Exception {
        // No return value, so just verify the controller returns OK.
        Mockito.doNothing().when(userService).changePassword(eq(1L), eq("newStrongPassword"));

        mockMvc.perform(put("/api/users/1/change-password")
                        .param("newPassword", "newStrongPassword"))
                .andExpect(status().isOk())
                .andExpect(content().string("Password updated successfully"));
    }

    @Test
    @DisplayName("Should handle user not found when changing password")
    void testChangePassword_userNotFound() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException("User not found with userId: 1"))
                .when(userService).changePassword(eq(1L), eq("newStrongPassword"));

        mockMvc.perform(put("/api/users/1/change-password")
                        .param("newPassword", "newStrongPassword"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Should delete user successfully")
    void testDeleteUser_success() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(eq(1L));

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User has been deleted Successfully"));
    }

    @Test
    @DisplayName("Should handle user not found on delete")
    void testDeleteUser_userNotFound() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException("User not found with id: 1"))
                .when(userService).deleteUser(eq(1L));

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }
}

