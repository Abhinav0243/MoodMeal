package com.example.MoodMeal.service;

import com.example.MoodMeal.model.Mood;
import com.example.MoodMeal.model.MoodType;
import com.example.MoodMeal.model.User;
import com.example.MoodMeal.repository.MoodRepository;
import com.example.MoodMeal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MoodService {

    private UserRepository userRepository;
    private MoodRepository moodRepository;

    @Autowired
    public MoodService(UserRepository userRepository,MoodRepository moodRepository){
        this.moodRepository = moodRepository;
        this.userRepository = userRepository;
    }

    public Mood recordMood(Long userId, MoodType moodType, String description){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with userId"));

        Mood mood = new Mood();
        mood.setUser(user);
        mood.setMoodType(moodType);
        mood.setDescription(description);
        mood.setDetectedAt(LocalDateTime.now());
        return moodRepository.save(mood);
    }

    public List<Mood> getMoodByUser(Long userId){
        return moodRepository.findByUserId(userId);
    }

    public Mood getMoodById(Long moodId){
        return moodRepository.findById(moodId)
                .orElseThrow(() -> new IllegalArgumentException("Mood not found with moodId : "+moodId));
    }

    public Mood updateMood(Long moodId,MoodType moodType,String description){
        Mood mood = this.getMoodById(moodId);
        mood.setMoodType(moodType);
        mood.setDescription(description);
        return moodRepository.save(mood);
    }

    public void deleteMood(Long moodId){
        if(!moodRepository.existsById(moodId)){
            throw new IllegalArgumentException("Mood not found with moodId: "+ moodId);
        }
        moodRepository.deleteById(moodId);
    }
}
