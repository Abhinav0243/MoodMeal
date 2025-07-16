package com.example.MoodMeal.controller;

import com.example.MoodMeal.model.Mood;
import com.example.MoodMeal.model.MoodType;
import com.example.MoodMeal.dto.MoodDTO;
import com.example.MoodMeal.service.MoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/moods")
public class MoodController {
    private final MoodService moodService;
    @Autowired
    public MoodController(MoodService moodService){
        this.moodService=moodService;
    }
    @PostMapping("/record")
    public ResponseEntity<MoodDTO> recordMood(@RequestParam Long userId, @RequestParam MoodType moodType,@RequestParam(required=false)String description){
        Mood mood=moodService.recordMood(userId, moodType,description);
        return ResponseEntity.ok(toDTO(mood));
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MoodDTO>> getMoodByUser(@PathVariable Long userId){
        List<Mood> moods=moodService.getMoodByUser(userId);
        return ResponseEntity.ok(moods.stream().map(this::toDTO).collect(Collectors.toList()));
    }
    @PutMapping("/{moodId}")
    public ResponseEntity<MoodDTO> updateMood(@PathVariable Long moodId,@RequestParam MoodType moodType,@RequestParam(required = false) String description){
        Mood updatedMood = moodService.updateMood(moodId,moodType,description);
        return ResponseEntity.ok(toDTO(updatedMood));
    }
    @DeleteMapping("/{moodId}")
    public ResponseEntity<String> deleteMood(@PathVariable Long moodId){
        moodService.deleteMood(moodId);
        return ResponseEntity.ok("Mood deleted Successfully");
    }
    // Mapping methods
    private MoodDTO toDTO(Mood mood) {
        MoodDTO dto = new MoodDTO();
        dto.setId(mood.getId());
        dto.setMoodType(mood.getMoodType());
        dto.setDescription(mood.getDescription());
        dto.setUserId(mood.getUser() != null ? mood.getUser().getId() : null);
        dto.setDetectedAt(mood.getDetectedAt());
        return dto;
    }
}
