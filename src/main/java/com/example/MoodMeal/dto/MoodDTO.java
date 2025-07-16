package com.example.MoodMeal.dto;


public class MoodDTO {

    private String moodType;      // e.g., "HAPPY", "ANGRY"
    private String description;   // optional user-entered note

    public String getMoodType() {
        return moodType;
    }

    public void setMoodType(String moodType) {
        this.moodType = moodType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
