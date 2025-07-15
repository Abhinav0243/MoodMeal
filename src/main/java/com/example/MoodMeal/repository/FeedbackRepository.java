package com.example.MoodMeal.repository;

import com.example.MoodMeal.model.Feedback;
import com.example.MoodMeal.model.Meal;
import com.example.MoodMeal.model.User;
import com.example.MoodMeal.model.MoodType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {


    List<Feedback> findByUser(User user);


    List<Feedback> findByMeal(Meal meal);


    List<Feedback> findByMoodType(MoodType moodType);


    List<Feedback> findByUserAndMeal(User user, Meal meal);


    List<Feedback> findByRating(Integer rating);
}
