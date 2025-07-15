package com.example.MoodMeal.repository;

import com.example.MoodMeal.model.Meal;
import com.example.MoodMeal.model.MoodType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {


    List<Meal> findBySuitableMoods(MoodType moodType);


    List<Meal> findByCusineIgnoreCase(String cusine);


    List<Meal> findByDietaryTagsContainingIgnoreCase(String dietaryTags);
}
