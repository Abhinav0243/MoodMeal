package com.example.MoodMeal.repository;

import com.example.MoodMeal.model.Mood;
import com.example.MoodMeal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MoodRepository extends JpaRepository<Mood, Long> {

    List<Mood> findByUser(User user);

    List<Mood> findByUserId(Long userId);
}
