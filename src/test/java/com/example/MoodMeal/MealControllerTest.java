package com.example.MoodMeal;

import com.example.MoodMeal.controller.MealController;
import com.example.MoodMeal.dto.MealDTO;
import com.example.MoodMeal.exception.ResourceNotFoundException;
import com.example.MoodMeal.model.Meal;
import com.example.MoodMeal.model.MoodType;
import com.example.MoodMeal.security.JWTAuthFilter;
import com.example.MoodMeal.security.JWTUtil;
import com.example.MoodMeal.service.CustomerDetailsService;
import com.example.MoodMeal.service.MealService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MealController.class)
@AutoConfigureMockMvc(addFilters = false)
class MealControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MealService mealService;

    @MockitoBean
    private JWTAuthFilter jwtAuthFilter;

    @MockitoBean
    private CustomerDetailsService customerDetailsService;

    @MockitoBean
    private JWTUtil jwtUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Should create meal successfully")
    void testAddMeal_success() throws Exception {
        Meal meal = new Meal();
        meal.setId(1L);
        meal.setName("Dosa");
        meal.setDescription("Tasty South Indian crepe");
        meal.setCuisine("Indian");
        meal.setCalories(200);
        meal.setAllergens("None");

        Mockito.when(mealService.addMeal(any(Meal.class))).thenReturn(meal);

        String reqBody = objectMapper.writeValueAsString(meal);

        mockMvc.perform(post("/api/meals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Dosa"));
    }

    @Test
    @DisplayName("Should list all meals")
    void testGetAllMeals_success() throws Exception {
        Meal meal1 = new Meal();
        meal1.setId(1L); meal1.setName("Dosa");
        Meal meal2 = new Meal();
        meal2.setId(2L); meal2.setName("Sushi");
        List<Meal> meals = Arrays.asList(meal1, meal2);

        Mockito.when(mealService.getAllMeals()).thenReturn(meals);

        mockMvc.perform(get("/api/meals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Dosa"))
                .andExpect(jsonPath("$[1].name").value("Sushi"));
    }

    @Test
    @DisplayName("Should get meal by id")
    void testGetMealById_success() throws Exception {
        Meal meal = new Meal();
        meal.setId(99L);
        meal.setName("Burger");
        Mockito.when(mealService.getMealById(99L)).thenReturn(meal);

        mockMvc.perform(get("/api/meals/99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(99))
                .andExpect(jsonPath("$.name").value("Burger"));
    }

    @Test
    @DisplayName("Should return 404 when meal not found by id")
    void testGetMealById_notFound() throws Exception {
        Mockito.when(mealService.getMealById(1000L))
                .thenThrow(new ResourceNotFoundException("Meal not found with mealId: 1000"));

        mockMvc.perform(get("/api/meals/1000"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Should update meal")
    void testUpdateMeal_success() throws Exception {
        Meal reqMeal = new Meal();
        reqMeal.setName("Paneer Wrap");
        reqMeal.setDescription("Veg wrap");

        Meal updatedMeal = new Meal();
        updatedMeal.setId(111L);
        updatedMeal.setName("Paneer Wrap");
        updatedMeal.setDescription("Veg wrap");

        Mockito.when(mealService.updateMeal(eq(111L), any(Meal.class))).thenReturn(updatedMeal);

        String reqBody = objectMapper.writeValueAsString(reqMeal);

        mockMvc.perform(put("/api/meals/111")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(111))
                .andExpect(jsonPath("$.name").value("Paneer Wrap"));
    }

    @Test
    @DisplayName("Should delete meal successfully")
    void testDeleteMeal_success() throws Exception {
        Mockito.doNothing().when(mealService).deleteMeal(222L);

        mockMvc.perform(delete("/api/meals/222"))
                .andExpect(status().isOk())
                .andExpect(content().string("Meal deleted Successfully"));
    }

    @Test
    @DisplayName("Should handle meal not found on delete")
    void testDeleteMeal_notFound() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException("Meal not found with mealId: 77"))
                .when(mealService).deleteMeal(77L);

        mockMvc.perform(delete("/api/meals/77"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Should get meals by mood")
    void testGetMealsByMood() throws Exception {
        Meal meal = new Meal(); meal.setId(1L); meal.setName("Pizza");
        List<Meal> meals = Arrays.asList(meal);

        Mockito.when(mealService.getMealByMood(MoodType.HAPPY)).thenReturn(meals);

        mockMvc.perform(get("/api/meals/by-mood")
                        .param("moodType", "HAPPY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Pizza"));
    }

    @Test
    @DisplayName("Should get meals by cuisine")
    void testGetMealsByCuisine() throws Exception {
        Meal meal = new Meal(); meal.setName("Tacos");
        List<Meal> meals = Arrays.asList(meal);

        Mockito.when(mealService.getMealByCuisine("mexican")).thenReturn(meals);

        mockMvc.perform(get("/api/meals/by-cuisine")
                        .param("cuisine", "mexican"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Tacos"));
    }

    @Test
    @DisplayName("Should get meals by dietary tag")
    void testGetMealsByDietaryTag() throws Exception {
        Meal meal = new Meal(); meal.setName("Gluten-Free Pasta");
        List<Meal> meals = Arrays.asList(meal);

        Mockito.when(mealService.getMealsByDietaryTag("gluten-free")).thenReturn(meals);

        mockMvc.perform(get("/api/meals/by-diet")
                        .param("tag", "gluten-free"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Gluten-Free Pasta"));
    }

    @Test
    @DisplayName("Should create meal from DTO")
    void testCreateMeal_fromDTO() throws Exception {
        MealDTO mealDTO = new MealDTO();
        mealDTO.setName("Salad Bowl");
        mealDTO.setCategory("Salad");
        mealDTO.setMoodType(MoodType.HAPPY.toString());

        // Mock just so the controller doesn't get stuck; actual save is void
        Mockito.doNothing().when(mealService).createMeal(any(MealDTO.class));

        String body = objectMapper.writeValueAsString(mealDTO);

        mockMvc.perform(post("/api/meals/dto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string("Meal created"));
    }
}
