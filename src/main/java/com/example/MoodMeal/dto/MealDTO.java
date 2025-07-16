package com.example.MoodMeal.dto;



public class MealDTO {

    private String name;        // e.g., "Pizza"
    private String category;    // e.g., "Dinner", "Lunch"
    private String moodType;    // e.g., "HAPPY", "SAD", "TIRED"

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

    public String getMoodType() {
        return moodType;
    }

    public void setMoodType(String moodType) {
        this.moodType = moodType;
    }
}
