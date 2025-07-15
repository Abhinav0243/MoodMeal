package com.example.MoodMeal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
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

    public User(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank String username) {
        this.username = username;
    }

    public @NotBlank @Email String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank @Email String email) {
        this.email = email;
    }

    public @NotBlank String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public @NotBlank String getFullName() {
        return fullName;
    }

    public void setFullName(@NotBlank String fullName) {
        this.fullName = fullName;
    }

    public @NotBlank Long getPhone() {
        return phone;
    }

    public void setPhone(@NotBlank Long phone) {
        this.phone = phone;
    }

    public String getDietaryPerferences() {
        return dietaryPerferences;
    }

    public void setDietaryPerferences(String dietaryPerferences) {
        this.dietaryPerferences = dietaryPerferences;
    }


    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public User(Long id, String username, String email, String password, Set<Role> roles, String fullName, Long phone, String dietaryPerferences, String allergies) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.fullName = fullName;
        this.phone = phone;
        this.dietaryPerferences = dietaryPerferences;
        this.allergies = allergies;
    }












}
