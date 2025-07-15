package com.example.MoodMeal.controller;

import com.example.MoodMeal.model.Meal;
import com.example.MoodMeal.model.MoodType;
import com.example.MoodMeal.service.SuggestionService;
import jdk.jshell.SourceCodeAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;


@RestController
@RequestMapping("/api/suggestions")
public class SuggestionController {
    private final SuggestionService suggestionService;
    @Autowired
    public SuggestionController(SuggestionService suggestionService){
        this.suggestionService=suggestionService;
    }
    @GetMapping
    public ResponseEntity<List<Meal>> suggestMeals(@RequestParam Long userId, @RequestParam MoodType moodType){
        List<Meal> suggestions=suggestionService.suggestMeals(userId, moodType);
        return ResponseEntity.ok(suggestions);
    }
    @GetMapping("/by-cuisine")
    public ResponseEntity<List<Meal>> suggestMealByCuisine(@RequestParam Long userId,@RequestParam MoodType moodType,@RequestParam String cuisine){
        List<Meal> suggestions=suggestionService.suggestMealByCuisine(userId,moodType,cuisine);
        return ResponseEntity.ok(suggestions);
    }

}
