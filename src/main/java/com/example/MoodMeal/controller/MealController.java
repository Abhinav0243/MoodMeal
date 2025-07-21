package com.example.MoodMeal.controller;

import com.example.MoodMeal.dto.MealDTO;
import com.example.MoodMeal.model.Meal;
import com.example.MoodMeal.model.MoodType;
import com.example.MoodMeal.service.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/meals")
public class MealController {
    private final MealService mealService;
    @Autowired
    public MealController(MealService mealService){
        this.mealService=mealService;
    }
//    @PostMapping("/addmeal")
//    public ResponseEntity<Meal> addMeal(@RequestBody Meal meal){
//        Meal createdMeal=mealService.addMeal(meal);
//        return ResponseEntity.ok(createdMeal);
//    }
    @GetMapping

    public ResponseEntity<List<Meal>> getAllMeals(){
        List<Meal> meals=mealService.getAllMeals();
        return ResponseEntity.ok(meals);
    }
    @GetMapping("/{mealId}")
    public ResponseEntity<Meal> getMealById(@PathVariable Long mealId){
        Meal meal=mealService.getMealById(mealId);
        return ResponseEntity.ok(meal);
    }
    @PutMapping("/{mealId}")
    public ResponseEntity<Meal> updateMeal(@PathVariable Long mealId,@RequestBody Meal updatedMeal){
        Meal meal=mealService.updateMeal(mealId, updatedMeal);
        return ResponseEntity.ok(meal);
    }
    @DeleteMapping("/{mealId}")
    public ResponseEntity<String> deleteMeal(@PathVariable Long mealId){
        mealService.deleteMeal(mealId);
        return ResponseEntity.ok("Meal deleted Successfully");
    }
    @GetMapping("/by-mood")
    public ResponseEntity<List<Meal>> getMealsByMood(@RequestParam MoodType moodType){
        List<Meal> meals=mealService.getMealByMood(moodType);


        return ResponseEntity.ok(meals);
    }
    @GetMapping("/by-cuisine")
    public ResponseEntity<List<Meal>> getMealsByCuisine(@RequestParam String cuisine){
        List<Meal> meals=mealService.getMealByCuisine(cuisine);
        return ResponseEntity.ok(meals);
    }
    @GetMapping("/by-diet")
    public ResponseEntity<List<Meal>> getMealsByDietaryTag(@RequestParam String tag){
        List<Meal> meals=mealService.getMealsByDietaryTag(tag);
        return ResponseEntity.ok(meals);
    }
    @PostMapping("/dto")
    public ResponseEntity<String> createMeal(@RequestBody MealDTO mealDTO) {
        mealService.createMeal(mealDTO);
        return ResponseEntity.ok("Meal created");
    }
}
