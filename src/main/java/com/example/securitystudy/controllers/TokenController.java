package com.example.securitystudy.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.securitystudy.dtos.requests.LoginRequest;
import com.example.securitystudy.dtos.responses.LoginResponse;
import com.example.securitystudy.services.LoginService;

@RestController
public class TokenController {

    private final LoginService loginService;

    public TokenController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = loginService.authenticate(request);
        return ResponseEntity.ok(response);
    }
    
}
