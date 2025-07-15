package com.example.MoodMeal.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "mood_history")
public class MoodHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MoodType moodType;

    @Column(length = 255)
    private String notes;

     @Column(nullable = false)
    private LocalDateTime recordedAt;

}
