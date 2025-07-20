package com.example.MoodMeal;

import com.example.MoodMeal.controller.MoodController;
import com.example.MoodMeal.exception.ResourceNotFoundException;
import com.example.MoodMeal.model.Mood;
import com.example.MoodMeal.model.MoodType;
import com.example.MoodMeal.model.User;
import com.example.MoodMeal.security.JWTAuthFilter;
import com.example.MoodMeal.security.JWTUtil;
import com.example.MoodMeal.service.CustomerDetailsService;
import com.example.MoodMeal.service.MoodService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MoodController.class)
@AutoConfigureMockMvc(addFilters = false)
class MoodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MoodService moodService;

    @MockitoBean
    private JWTAuthFilter jwtAuthFilter;

    @MockitoBean
    private CustomerDetailsService customerDetailsService;

    @MockitoBean
    private JWTUtil jwtUtil;

    @Test
    @DisplayName("Should record mood successfully")
    void testRecordMood_success() throws Exception {
        MoodType testType = MoodType.HAPPY; // Use your enum value here!
        User user = new User();
        user.setId(42L);
        Mood mood = new Mood();
        mood.setId(100L);
        mood.setUser(user);
        mood.setMoodType(testType);
        mood.setDescription("Great day!");
        mood.setDetectedAt(LocalDateTime.now());

        Mockito.when(moodService.recordMood(eq(42L), eq(testType), eq("Great day!")))
                .thenReturn(mood);

        mockMvc.perform(post("/api/moods/record")
                        .param("userId", "42")
                        .param("moodType", "HAPPY")
                        .param("description", "Great day!"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.moodType").value("HAPPY"))
                .andExpect(jsonPath("$.description").value("Great day!"));
    }

    @Test
    @DisplayName("Should handle user not found when recording mood")
    void testRecordMood_userNotFound() throws Exception {
        Mockito.when(moodService.recordMood(eq(999L), eq(MoodType.SAD), any()))
                .thenThrow(new ResourceNotFoundException("User not found with userId"));

        mockMvc.perform(post("/api/moods/record")
                        .param("userId", "999")
                        .param("moodType", "SAD")
                        .param("description", "Feeling alone"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Should fetch all moods for a user")
    void testGetMoodByUser_success() throws Exception {
        User user = new User();
        user.setId(55L);

        Mood mood1 = new Mood();
        mood1.setId(1L);
        mood1.setUser(user);
        mood1.setMoodType(MoodType.HAPPY);
        mood1.setDescription("Sunshine!");
        mood1.setDetectedAt(LocalDateTime.now());

        Mood mood2 = new Mood();
        mood2.setId(2L);
        mood2.setUser(user);
        mood2.setMoodType(MoodType.SAD);
        mood2.setDescription("Rainy day.");
        mood2.setDetectedAt(LocalDateTime.now());

        List<Mood> moods = Arrays.asList(mood1, mood2);
        Mockito.when(moodService.getMoodByUser(55L)).thenReturn(moods);

        mockMvc.perform(get("/api/moods/user/55"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].moodType").value("HAPPY"))
                .andExpect(jsonPath("$[1].moodType").value("SAD"));
    }

    @Test
    @DisplayName("Should update mood successfully")
    void testUpdateMood_success() throws Exception {
        Mood updatedMood = new Mood();
        updatedMood.setId(5L);
        updatedMood.setMoodType(MoodType.ANGRY);
        updatedMood.setDescription("Traffic jam");

        Mockito.when(moodService.updateMood(eq(5L), eq(MoodType.ANGRY), eq("Traffic jam")))
                .thenReturn(updatedMood);

        mockMvc.perform(put("/api/moods/5")
                        .param("moodType", "ANGRY")
                        .param("description", "Traffic jam"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.moodType").value("ANGRY"))
                .andExpect(jsonPath("$.description").value("Traffic jam"));
    }

    @Test
    @DisplayName("Should handle mood not found on update")
    void testUpdateMood_notFound() throws Exception {
        Mockito.when(moodService.updateMood(eq(123L), eq(MoodType.HAPPY), any()))
                .thenThrow(new ResourceNotFoundException("Mood not found with moodId : 123"));

        mockMvc.perform(put("/api/moods/123")
                        .param("moodType", "HAPPY")
                        .param("description", "Updated!"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Should delete mood successfully")
    void testDeleteMood_success() throws Exception {
        Mockito.doNothing().when(moodService).deleteMood(7L);

        mockMvc.perform(delete("/api/moods/7"))
                .andExpect(status().isOk())
                .andExpect(content().string("Mood deleted Successfully"));
    }

    @Test
    @DisplayName("Should handle mood not found on delete")
    void testDeleteMood_notFound() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException("Mood not found with moodId: 1000"))
                .when(moodService).deleteMood(1000L);

        mockMvc.perform(delete("/api/moods/1000"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }
}