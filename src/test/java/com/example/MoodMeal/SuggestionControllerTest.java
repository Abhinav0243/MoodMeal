package com.example.MoodMeal;

import com.example.MoodMeal.controller.SuggestionController;
import com.example.MoodMeal.exception.ResourceNotFoundException;
import com.example.MoodMeal.model.Meal;
import com.example.MoodMeal.model.MoodType;
import com.example.MoodMeal.security.JWTAuthFilter;
import com.example.MoodMeal.security.JWTUtil;
import com.example.MoodMeal.service.CustomerDetailsService;
import com.example.MoodMeal.service.SuggestionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SuggestionController.class)
@AutoConfigureMockMvc(addFilters = false)
class SuggestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SuggestionService suggestionService;

    @MockitoBean
    private JWTAuthFilter jwtAuthFilter;

    @MockitoBean
    private CustomerDetailsService customerDetailsService;

    @MockitoBean
    private JWTUtil jwtUtil;

    @Test
    @DisplayName("Should suggest meals for user and mood")
    void testSuggestMeals_success() throws Exception {
        Meal meal = new Meal();
        meal.setId(1L);
        meal.setName("Salad");

        List<Meal> suggestions = Collections.singletonList(meal);

        Mockito.when(suggestionService.suggestMeals(42L, MoodType.HAPPY)).thenReturn(suggestions);

        mockMvc.perform(get("/api/suggestions")
                        .param("userId", "42")
                        .param("moodType", "HAPPY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Salad"));
    }

    @Test
    @DisplayName("Should handle user not found in suggestions")
    void testSuggestMeals_userNotFound() throws Exception {
        Mockito.when(suggestionService.suggestMeals(99L, MoodType.SAD))
                .thenThrow(new ResourceNotFoundException("User not found with userId: 99"));

        mockMvc.perform(get("/api/suggestions")
                        .param("userId", "99")
                        .param("moodType", "SAD"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Should suggest meals by cuisine for user and mood")
    void testSuggestMealByCuisine_success() throws Exception {
        Meal meal1 = new Meal();
        meal1.setId(2L);
        meal1.setName("Paneer Butter Masala");

        Meal meal2 = new Meal();
        meal2.setId(3L);
        meal2.setName("Dal Tadka");

        List<Meal> suggestions = Arrays.asList(meal1, meal2);

        Mockito.when(suggestionService.suggestMealByCuisine(24L, MoodType.HAPPY, "Indian")).thenReturn(suggestions);

        mockMvc.perform(get("/api/suggestions/by-cuisine")
                        .param("userId", "24")
                        .param("moodType", "HAPPY")
                        .param("cuisine", "Indian"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Paneer Butter Masala"))
                .andExpect(jsonPath("$[1].name").value("Dal Tadka"));
    }

    @Test
    @DisplayName("Should handle user not found in suggestMealByCuisine")
    void testSuggestMealByCuisine_userNotFound() throws Exception {
        Mockito.when(suggestionService.suggestMealByCuisine(56L, MoodType.ANGRY, "Mexican"))
                .thenThrow(new ResourceNotFoundException("User not found with userId: 56"));

        mockMvc.perform(get("/api/suggestions/by-cuisine")
                        .param("userId", "56")
                        .param("moodType", "ANGRY")
                        .param("cuisine", "Mexican"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }
}
