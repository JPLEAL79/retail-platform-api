package com.jp.testplatformapi.controller;

import com.jp.testplatformapi.repository.UserRepository;
import com.jp.testplatformapi.entity.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Simple controller with DB integration
@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // GET all users
    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    // POST create user
    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }
}