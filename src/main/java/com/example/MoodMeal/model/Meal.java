package com.example.MoodMeal.model;

import jakarta.persistence.*;


import java.util.*;

@Entity
@Table(name = "meals")

public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    private String category;
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


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

    public String getMoodType() {
        return moodType;
    }

    public void setMoodType(String moodType) {
        this.moodType = moodType;
    }

    private String moodType; // ✅ this must exist


    @ElementCollection(targetClass = MoodType.class)
    @CollectionTable(name = "meal_moods", joinColumns = @JoinColumn(name = "meal_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "mood_type")
    private Set<MoodType> suitableMoods = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public Set<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getDietaryTags() {
        return dietaryTags;
    }

    public void setDietaryTags(String dietaryTags) {
        this.dietaryTags = dietaryTags;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public String getAllergens() {
        return allergens;
    }

    public void setAllergens(String allergens) {
        this.allergens = allergens;
    }

    public Set<MoodType> getSuitableMoods() {
        return suitableMoods;
    }

    public void setSuitableMoods(Set<MoodType> suitableMoods) {
        this.suitableMoods = suitableMoods;
    }
}
