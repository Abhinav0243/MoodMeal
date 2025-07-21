package com.example.MoodMeal.dto;


import com.example.MoodMeal.model.MoodType;

public class MealDTO {

    private String name;        // e.g., "Pizza"
    private String category;    // e.g., "Dinner", "Lunch"
    private MoodType moodType;    // e.g., "HAPPY", "SAD", "TIRED"
    private String cuisine;
    private String dietaryTags;
    private String description;
    private String Allergens;

    public String getAllergens() {
        return Allergens;
    }

    public void setAllergens(String allergens) {
        Allergens = allergens;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getDietaryTags() {
        return dietaryTags;
    }

    public void setDietaryTags(String dietaryTags) {
        this.dietaryTags = dietaryTags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMoodType(MoodType moodType) {
        this.moodType = moodType;
    }

    public MoodType getMoodType() {
        return moodType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
