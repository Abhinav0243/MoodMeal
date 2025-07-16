package com.example.MoodMeal.controller;

import com.example.MoodMeal.dto.MealDTO;
import com.example.MoodMeal.model.Meal;
import com.example.MoodMeal.model.MoodType;
import com.example.MoodMeal.dto.MealDTO;
import com.example.MoodMeal.service.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/meals")
public class MealController {
    private final MealService mealService;
    @Autowired
    public MealController(MealService mealService){
        this.mealService=mealService;
    }
    @PostMapping
    public ResponseEntity<MealDTO> addMeal(@RequestBody MealDTO mealDTO){
        Meal createdMeal=mealService.addMeal(toEntity(mealDTO));
        return ResponseEntity.ok(toDTO(createdMeal));
    }
    @GetMapping
    public ResponseEntity<List<MealDTO>> getAllMeals(){
        List<Meal> meals=mealService.getAllMeals();
        return ResponseEntity.ok(meals.stream().map(this::toDTO).collect(Collectors.toList()));
    }
    @GetMapping("/{mealId}")
    public ResponseEntity<MealDTO> getMealById(@PathVariable Long mealId){
        Meal meal=mealService.getMealById(mealId);
        return ResponseEntity.ok(toDTO(meal));
    }
    @PutMapping("/{mealId}")
    public ResponseEntity<MealDTO> updateMeal(@PathVariable Long mealId,@RequestBody MealDTO updatedMealDTO){
        Meal meal=mealService.updateMeal(mealId, toEntity(updatedMealDTO));
        return ResponseEntity.ok(toDTO(meal));
    }
    @DeleteMapping("/{mealId}")
    public ResponseEntity<String> deleteMeal(@PathVariable Long mealId){
        mealService.deleteMeal(mealId);
        return ResponseEntity.ok("Meal deleted Successfully");
    }
    @GetMapping("/by-mood")
    public ResponseEntity<List<MealDTO>> getMealsByMood(@RequestParam MoodType moodType){
        List<Meal> meals=mealService.getMealByMood(moodType);
        return ResponseEntity.ok(meals.stream().map(this::toDTO).collect(Collectors.toList()));
    }
    @GetMapping("/by-cuisine")
    public ResponseEntity<List<MealDTO>> getMealsByCuisine(@RequestParam String cuisine){
        List<Meal> meals=mealService.getMealByCuisine(cuisine);
        return ResponseEntity.ok(meals.stream().map(this::toDTO).collect(Collectors.toList()));
    }
    @GetMapping("/by-diet")
    public ResponseEntity<List<MealDTO>> getMealsByDietaryTag(@RequestParam String tag){
        List<Meal> meals=mealService.getMealsByDietaryTag(tag);
        return ResponseEntity.ok(meals.stream().map(this::toDTO).collect(Collectors.toList()));
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
    private Meal toEntity(MealDTO dto) {
        Meal meal = new Meal();
        meal.setId(dto.getId());
        meal.setName(dto.getName());
        meal.setDescription(dto.getDescription());
        meal.setCuisine(dto.getCuisine());
        meal.setIngredients(dto.getIngredients());
        meal.setDietaryTags(dto.getDietaryTags());
        meal.setCalories(dto.getCalories());
        meal.setAllergens(dto.getAllergens());
        meal.setSuitableMoods(dto.getSuitableMoods());
        return meal;
    }
    @PostMapping("/meals")
    public ResponseEntity<String> createMeal(@RequestBody MealDTO mealDTO) {
        mealService.createMeal(mealDTO);
        return ResponseEntity.ok("Meal created");
    }
}
