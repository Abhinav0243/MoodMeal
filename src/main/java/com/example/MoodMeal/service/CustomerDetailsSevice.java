package com.example.MoodMeal.service;

import com.example.MoodMeal.model.Role;
import com.example.MoodMeal.model.User;
import com.example.MoodMeal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomerDetailsSevice {

    private UserRepository userRepository;

    @Autowired
    public CustomerDetailsSevice(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // You can also allow login by email here if you want
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles())
        );
    }

    private Set<SimpleGrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toSet());
    }

}
