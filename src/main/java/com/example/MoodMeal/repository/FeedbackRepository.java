package com.example.MoodMeal.repository;

import com.example.MoodMeal.model.Feedback;
import com.example.MoodMeal.model.Meal;
import com.example.MoodMeal.model.User;
import com.example.MoodMeal.model.MoodType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    // Get all feedbacks by a specific user
    List<Feedback> findByUser(User user);

    // Get all feedbacks for a specific meal
    List<Feedback> findByMeal(Meal meal);

    // Get all feedbacks by a specific mood type
    List<Feedback> findByMoodType(MoodType moodType);

    // Get all feedbacks by user and meal
    List<Feedback> findByUserAndMeal(User user, Meal meal);

    // Get all feedbacks with a specific rating
    List<Feedback> findByRating(Integer rating);
}
