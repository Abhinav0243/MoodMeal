package com.example.MoodMeal.repository;

import com.example.MoodMeal.model.Meal;
import com.example.MoodMeal.model.MoodType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {

    // Find meals by mood
    List<Meal> findBySuitableMoods(MoodType moodType);

    // Find meals by cuisine
    List<Meal> findByCusineIgnoreCase(String cusine);

    // Find meals containing specific dietary tags
    List<Meal> findByDietaryTagsContainingIgnoreCase(String dietaryTags);
}
