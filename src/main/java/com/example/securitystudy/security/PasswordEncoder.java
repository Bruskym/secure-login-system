package com.example.securitystudy.security;

public interface PasswordEncoder {
    String encode(String rawPassword);
}
