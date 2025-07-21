package com.example.MoodMeal.controller;

import com.example.MoodMeal.model.Mood;
import com.example.MoodMeal.model.MoodType;
import com.example.MoodMeal.service.MoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.MoodMeal.dto.MoodRecordRequest;

import java.util.List;

@RestController
@RequestMapping("/api/moods")
public class MoodController {
    private final MoodService moodService;
    @Autowired
    public MoodController(MoodService moodService){
        this.moodService=moodService;
    }

    @PostMapping("/record")
    public ResponseEntity<Mood> recordMood(@RequestBody MoodRecordRequest request){
        MoodType moodType=request.getMoodType();
        String description=request.getDescription();
        Long userId=request.getUserId();
        Mood mood=moodService.recordMood(userId, moodType,description);
        return ResponseEntity.ok(mood);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Mood>> getMoodByUser(@PathVariable Long userId){
        List<Mood> moods=moodService.getMoodByUser(userId);
        return ResponseEntity.ok(moods);
    }
    @PutMapping("/{moodId}")
    public ResponseEntity<Mood> updateMood(@PathVariable Long moodId,@RequestParam MoodType moodType,@RequestParam(required = false) String description){
        Mood updatedMood = moodService.updateMood(moodId,moodType,description);
        return ResponseEntity.ok(updatedMood);
    }
    @DeleteMapping("/{moodId}")
    public ResponseEntity<String> deleteMood(@PathVariable Long moodId){
        moodService.deleteMood(moodId);
        return ResponseEntity.ok("Mood deleted Successfully");
    }
}
