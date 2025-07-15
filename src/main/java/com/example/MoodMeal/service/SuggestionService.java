package com.example.MoodMeal.service;

import com.example.MoodMeal.repository.MealRepository;
import com.example.MoodMeal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.MoodMeal.model.Meal;
import com.example.MoodMeal.model.MoodType;
import com.example.MoodMeal.model.User;

import javax.swing.plaf.PanelUI;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SuggestionService {

    private final MealRepository mealRepository;
    private final UserRepository userRepository;

    @Autowired
    public SuggestionService(MealRepository mealRepository, UserRepository userRepository){
        this.mealRepository=mealRepository;
        this.userRepository=userRepository;
    }

    public List<Meal> suggestMeals(Long userId, MoodType moodType){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with userId: "+userId));

        List<Meal> meals = mealRepository.findBySuitableMoods(moodType);

        String dietaryPrefs = user.getDietaryPerferences();
        String allergies = user.getAllergies();

        return meals.stream()
                .filter(meal -> dietaryPrefs == null || dietaryPrefs.isBlank() || (meal.getDietaryTags() != null && meal.getDietaryTags().toLowerCase().contains(dietaryPrefs.toLowerCase())))
                .filter(meal -> allergies == null || allergies.isBlank() || (meal.getAllergens() == null || !meal.getAllergens().toLowerCase().contains(allergies.toLowerCase())))
                .collect(Collectors.toList());
    }

    public List<Meal> suggestMealByCuisine(Long userId, MoodType moodType, String cuisine){
        List<Meal> moodMeals = suggestMeals(userId, moodType);
        return moodMeals.stream()
                .filter(meal -> cuisine == null || cuisine.isBlank() ||
                        (meal.getCuisine() != null && meal.getCuisine().equalsIgnoreCase(cuisine)))
                .collect(Collectors.toList());
    }
}
