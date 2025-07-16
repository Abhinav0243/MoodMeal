package com.example.MoodMeal.controller;

import com.example.MoodMeal.dto.FeedbackDTO;
import com.example.MoodMeal.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<FeedbackDTO> addFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        FeedbackDTO created = feedbackService.addFeedback(feedbackDTO);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FeedbackDTO>> getFeedbackByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(feedbackService.getFeedbackByUser(userId));
    }

    @GetMapping("/meal/{mealId}")
    public ResponseEntity<List<FeedbackDTO>> getFeedbackByMeal(@PathVariable Long mealId) {
        return ResponseEntity.ok(feedbackService.getFeedbackByMeal(mealId));
    }
} 