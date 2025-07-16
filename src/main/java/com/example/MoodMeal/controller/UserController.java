package com.example.MoodMeal.controller;

import com.example.MoodMeal.model.User;
import com.example.MoodMeal.dto.UserDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.MoodMeal.service.UserService;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService=userService;
    }
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserDTO userDTO){
        User createdUser=userService.registerUser(toEntity(userDTO));
        return ResponseEntity.ok(toDTO(createdUser));
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id){
        User user=userService.getUserById(id);
        return ResponseEntity.ok(toDTO(user));
    }
    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username){
        User user=userService.getUserByUsername(username);
        return ResponseEntity.ok(toDTO(user));
    }
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email){
        User user=userService.getUserByEmail(email);
        return ResponseEntity.ok(toDTO(user));
    }
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id,@RequestBody UserDTO updatedUserDTO){
        User user=userService.updateUser(id,toEntity(updatedUserDTO));
        return ResponseEntity.ok(toDTO(user));
    }
    @PutMapping("/{id}/change-password")
    public ResponseEntity<String> changePassword(@PathVariable Long id,@RequestParam String newPassword){
        userService.changePassword(id, newPassword);
        return ResponseEntity.ok("Password updated successfully");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok("User has been deleted Successfully");
    }

    // Mapping methods
    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRoles());
        dto.setFullName(user.getFullName());
        dto.setPhone(user.getPhone());
        dto.setDietaryPerferences(user.getDietaryPerferences());
        dto.setAllergies(user.getAllergies());
        return dto;
    }
    private User toEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRoles(dto.getRoles());
        user.setFullName(dto.getFullName());
        user.setPhone(dto.getPhone());
        user.setDietaryPerferences(dto.getDietaryPerferences());
        user.setAllergies(dto.getAllergies());
        // Password is not set here for security reasons
        return user;
    }
}
