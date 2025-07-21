package com.example.MoodMeal.dto;

import com.example.MoodMeal.model.MoodType;

public class MoodRecordRequest {
    private Long userId;
    private String description;
    private MoodType moodType;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MoodType getMoodType() {
        return moodType;
    }

    public void setMoodType(MoodType moodType) {
        this.moodType = moodType;
    }
}
