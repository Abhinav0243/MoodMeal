package com.example.MoodMeal.service;

import com.example.MoodMeal.exception.ResourceNotFoundException;
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
                .orElseThrow(() -> new ResourceNotFoundException("User not found with userId: "+userId));

        List<Meal> meals = mealRepository.findByMoodType(moodType);

        // Logging what we got
        System.out.println("Fetched " + meals.size() + " meals for mood: " + moodType);
        meals.forEach(meal -> System.out.println("Meal: " + meal.getName() + ", MoodType: " + meal.getMoodType()));

        String dietaryPrefs = user.getDietaryPreferences();
        String allergies = user.getAllergies();

        System.out.println("User Dietary Prefs: " + dietaryPrefs);
        System.out.println("User Allergens: " + allergies);



        List<Meal> filtered = meals.stream()
                .filter(meal -> {
                    String mealTags = meal.getDietaryTags() != null ? meal.getDietaryTags().toLowerCase() : "";

                    boolean dietaryMatch;
                    if (dietaryPrefs == null || dietaryPrefs.equalsIgnoreCase("None")) {
                        dietaryMatch = true; // No preference, accept all
                    } else {
                        String pref = dietaryPrefs.toLowerCase();
                        switch (pref) {
                            case "vegan":
                                dietaryMatch = mealTags.contains("vegan");
                                break;
                            case "vegetarian":
                                dietaryMatch = mealTags.contains("vegetarian") || mealTags.contains("vegan");
                                break;
                            case "non-vegetarian":
                                dietaryMatch = true; // Accept all types
                                break;
                            default:
                                dietaryMatch = false; // Unknown preference
                        }
                    }

                    boolean allergenSafe = allergies == null || allergies.equalsIgnoreCase("None") ||
                            (meal.getAllergens() == null || !meal.getAllergens().toLowerCase().contains(allergies.toLowerCase()));

                    System.out.println("Meal: " + meal.getName() + " => DietaryMatch: " + dietaryMatch + ", AllergenSafe: " + allergenSafe);

                    return dietaryMatch && allergenSafe;
                })
                .toList();

        System.out.println("Filtered Meal Count: " + filtered.size());
        return filtered;
    }

    public List<Meal> suggestMealByCuisine(Long userId, MoodType moodType, String cuisine){
        List<Meal> moodMeals = suggestMeals(userId, moodType);
        return moodMeals.stream()
                .filter(meal -> cuisine == null || cuisine.isBlank() ||
                        (meal.getCuisine() != null && meal.getCuisine().equalsIgnoreCase(cuisine)))
                .collect(Collectors.toList());
    }
}
