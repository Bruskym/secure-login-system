package com.example.securitystudy.services;

import com.example.securitystudy.dtos.requests.RegisterRequest;
import com.example.securitystudy.dtos.responses.RegisterResponse;

public interface RegisterUserService {
    RegisterResponse registerUser(RegisterRequest request);
}
