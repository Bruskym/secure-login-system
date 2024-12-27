package com.example.securitystudy.services;

import java.util.List;

import com.example.securitystudy.entities.User;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(String id);
}
