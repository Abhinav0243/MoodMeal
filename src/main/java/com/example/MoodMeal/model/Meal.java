package com.example.MoodMeal.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "meals")
@Data
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 100)
    private String name;

    @Column(nullable = false,length = 200)
    private String description;

    @Column(length = 100)
    private String cuisine;

    @ElementCollection
    @CollectionTable(name = "meal_ingredients", joinColumns = @JoinColumn(name = "meal_id"))
    @Column(name = "ingredient")
    private Set<String> ingredients = new HashSet<>();

    @Column(length = 255)
    private String dietaryTags;

    @Column
    private Integer calories;

    @Column(length = 255)
    private String allergens;

    @ElementCollection(targetClass = MoodType.class)
    @CollectionTable(name = "meal_moods", joinColumns = @JoinColumn(name = "meal_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "mood_type")
    private Set<MoodType> suitableMoods = new HashSet<>();


}
