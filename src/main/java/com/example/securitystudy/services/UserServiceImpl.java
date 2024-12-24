package com.example.securitystudy.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.securitystudy.entities.User;
import com.example.securitystudy.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService{
    
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepository.findById(UUID.fromString(id));
    }
    
}
