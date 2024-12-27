package com.example.securitystudy.services;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    public User getUserById(String id) {
        User user = userRepository.findById(UUID.fromString(id)).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return user;
    }
    
}
