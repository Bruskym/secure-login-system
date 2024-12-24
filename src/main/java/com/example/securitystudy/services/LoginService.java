package com.example.securitystudy.services;

import com.example.securitystudy.dtos.requests.LoginRequest;
import com.example.securitystudy.dtos.responses.LoginResponse;

public interface LoginService {
    public LoginResponse authenticate(LoginRequest request);
}
