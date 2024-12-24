package com.example.securitystudy.services;

import java.util.List;
import java.util.Optional;

import com.example.securitystudy.entities.User;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(String id);
}
