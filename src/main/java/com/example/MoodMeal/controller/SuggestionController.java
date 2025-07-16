package com.example.MoodMeal.controller;

import com.example.MoodMeal.model.Meal;
import com.example.MoodMeal.model.MoodType;
import com.example.MoodMeal.dto.MealDTO;
import com.example.MoodMeal.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/suggestions")
public class SuggestionController {
    private final SuggestionService suggestionService;
    @Autowired
    public SuggestionController(SuggestionService suggestionService){
        this.suggestionService=suggestionService;
    }
    @GetMapping
    public ResponseEntity<List<MealDTO>> suggestMeals(@RequestParam Long userId, @RequestParam MoodType moodType){
        List<Meal> suggestions=suggestionService.suggestMeals(userId, moodType);
        return ResponseEntity.ok(suggestions.stream().map(this::toDTO).collect(Collectors.toList()));
    }
    @GetMapping("/by-cuisine")
    public ResponseEntity<List<MealDTO>> suggestMealByCuisine(@RequestParam Long userId,@RequestParam MoodType moodType,@RequestParam String cuisine){
        List<Meal> suggestions=suggestionService.suggestMealByCuisine(userId,moodType,cuisine);
        return ResponseEntity.ok(suggestions.stream().map(this::toDTO).collect(Collectors.toList()));
    }
    // Mapping methods
    private MealDTO toDTO(Meal meal) {
        MealDTO dto = new MealDTO();
        dto.setId(meal.getId());
        dto.setName(meal.getName());
        dto.setDescription(meal.getDescription());
        dto.setCuisine(meal.getCuisine());
        dto.setIngredients(meal.getIngredients());
        dto.setDietaryTags(meal.getDietaryTags());
        dto.setCalories(meal.getCalories());
        dto.setAllergens(meal.getAllergens());
        dto.setSuitableMoods(meal.getSuitableMoods());
        return dto;
    }
}
