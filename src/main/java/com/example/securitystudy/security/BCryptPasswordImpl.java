package com.example.securitystudy.security;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordImpl implements PasswordEncoder{

    private final int logRounds = 12;

    @Override
    public String encode(String rawPassword) {
        String salt = BCrypt.gensalt(logRounds);
        return BCrypt.hashpw(rawPassword, salt);
    }
    
}
