package com.example.MoodMeal.controller;

import com.example.MoodMeal.model.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.MoodMeal.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService=userService;
    }
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user){
        User createdUser=userService.registerUser(user);
        return ResponseEntity.ok(createdUser);
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        User user=userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username){
        User user=userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email){
        User user=userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id,@RequestBody User updateduser){
        User user=userService.updateUser(id,updateduser);
        return ResponseEntity.ok(user);
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


}
