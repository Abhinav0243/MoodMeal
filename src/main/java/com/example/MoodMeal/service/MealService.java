package com.example.MoodMeal.service;

import com.example.MoodMeal.dto.MealDTO;
import com.example.MoodMeal.exception.ResourceNotFoundException;
import com.example.MoodMeal.model.Meal;
import com.example.MoodMeal.model.MoodType;
import com.example.MoodMeal.repository.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
@Service
public class MealService {

    private MealRepository mealRepository;

    @Autowired
    public MealService(MealRepository mealRepository){
        this.mealRepository= mealRepository;
    }

    public Meal addMeal(Meal meal){
        return mealRepository.save(meal);
    }
    public List<Meal> getAllMeals(){
        return mealRepository.findAll();
    }
    public Meal getMealById(Long mealId){
        return mealRepository.findById(mealId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found with mealId: " +mealId));
    }

    public Meal updateMeal(Long mealId,Meal updatedMeal){
        Meal meal = getMealById(mealId);
        meal.setName(updatedMeal.getName());
        meal.setId(updatedMeal.getId());
        meal.setDescription(updatedMeal.getDescription());
        meal.setCuisine(updatedMeal.getCuisine());
        meal.setCalories(updatedMeal.getCalories());
        meal.setAllergens(updatedMeal.getAllergens());
        meal.setDietaryTags(updatedMeal.getDietaryTags());
        meal.setIngredients(updatedMeal.getIngredients());
//        meal.setSuitableMoods(updatedMeal.getSuitableMoods());
        return mealRepository.save(meal);
    }

    public void deleteMeal(Long mealId){
        if(!mealRepository.existsById(mealId)){
            throw new ResourceNotFoundException("Meal not found with mealId: "+ mealId);
        }
    }

    public List<Meal> getMealByMood(MoodType moodType){
        System.out.println("DEBUG: Searching for meals with MoodType: " + moodType);
        List<Meal> meals = mealRepository.findByMoodType(moodType);
        System.out.println("DEBUG: Found " + meals.size() + " meals for MoodType: " + moodType); // Add this line
        return meals;

//        return mealRepository.findByMoodType(moodType);
    }
    public List<Meal> getMealByCuisine(String cuisine){
        return mealRepository.findByCuisineIgnoreCase(cuisine);
    }
    public List<Meal> getMealsByDietaryTag(String dietaryTag) {
        return mealRepository.findByDietaryTagsContainingIgnoreCase(dietaryTag);
    }
    public void createMeal(MealDTO mealDTO) {
        Meal meal = new Meal();
        meal.setName(mealDTO.getName());
        meal.setCategory(mealDTO.getCategory());
        // Assuming you have an enum or string moodType field
        meal.setMoodType(mealDTO.getMoodType());
        meal.setCuisine(mealDTO.getCuisine());                // ✅ add this
        meal.setDietaryTags(mealDTO.getDietaryTags());        // ✅ add this
        meal.setDescription(mealDTO.getDescription());
        meal.setAllergens(mealDTO.getAllergens());
        mealRepository.save(meal);
    }
}
