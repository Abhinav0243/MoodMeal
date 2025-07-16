package com.example.MoodMeal.service;

import com.example.MoodMeal.model.Feedback;
import com.example.MoodMeal.model.Meal;
import com.example.MoodMeal.model.User;
import com.example.MoodMeal.model.MoodType;
import com.example.MoodMeal.repository.FeedbackRepository;
import com.example.MoodMeal.repository.MealRepository;
import com.example.MoodMeal.repository.UserRepository;
import com.example.MoodMeal.dto.FeedbackDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MealRepository mealRepository;

    public FeedbackDTO addFeedback(FeedbackDTO dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Meal meal = mealRepository.findById(dto.getMealId()).orElseThrow(() -> new IllegalArgumentException("Meal not found"));
        Feedback feedback = new Feedback();
        feedback.setUser(user);
        feedback.setMeal(meal);
        feedback.setMoodType(dto.getMoodType());
        feedback.setRating(dto.getRating());
        feedback.setComment(dto.getComment());
        feedback.setSubmittedAt(dto.getSubmittedAt() != null ? dto.getSubmittedAt() : java.time.LocalDateTime.now());
        Feedback saved = feedbackRepository.save(feedback);
        return toDTO(saved);
    }

    public List<FeedbackDTO> getFeedbackByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return feedbackRepository.findByUser(user).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<FeedbackDTO> getFeedbackByMeal(Long mealId) {
        Meal meal = mealRepository.findById(mealId).orElseThrow(() -> new IllegalArgumentException("Meal not found"));
        return feedbackRepository.findByMeal(meal).stream().map(this::toDTO).collect(Collectors.toList());
    }

    private FeedbackDTO toDTO(Feedback feedback) {
        FeedbackDTO dto = new FeedbackDTO();
        dto.setId(feedback.getId());
        dto.setUserId(feedback.getUser().getId());
        dto.setMealId(feedback.getMeal().getId());
        dto.setMoodType(feedback.getMoodType());
        dto.setRating(feedback.getRating());
        dto.setComment(feedback.getComment());
        dto.setSubmittedAt(feedback.getSubmittedAt());
        return dto;
    }
} 