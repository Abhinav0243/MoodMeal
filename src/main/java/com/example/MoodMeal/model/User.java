package com.example.MoodMeal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.*;
@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column( unique = true,nullable = false,length = 50)
    private String username;

    @NotBlank
    @Email
    @Column(nullable = false,unique = false,length = 100)
    private String email;

    @NotBlank
    @Column(nullable = false,length = 200)
    private String password;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<Role> roles = new HashSet<>();

    @NotBlank
    @Column(nullable = false,length = 100)
    private String fullName;

    @NotBlank
    @Column(length = 25)
    private Long phone;

    @Column(length = 200)
    private String dietaryPerferences;

    @Column(length = 200)
    private String allergies;
}
