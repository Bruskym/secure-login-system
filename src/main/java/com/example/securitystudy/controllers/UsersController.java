package com.example.securitystudy.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.securitystudy.dtos.requests.RegisterRequest;
import com.example.securitystudy.dtos.responses.RegisterResponse;
import com.example.securitystudy.entities.User;
import com.example.securitystudy.services.RegisterUserService;
import com.example.securitystudy.services.UserService;


@RestController
@RequestMapping("/user")
public class UsersController {

    private final UserService userService;

    private final RegisterUserService registerUserService;

    public UsersController(UserService userService, RegisterUserService registerUserService){
        this.userService = userService;
        this.registerUserService = registerUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerNewUser(@RequestBody RegisterRequest request) {
        RegisterResponse response = registerUserService.registerUser(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listAll")
    public ResponseEntity<List<User>> listUsers() {
        List<User> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }
}
