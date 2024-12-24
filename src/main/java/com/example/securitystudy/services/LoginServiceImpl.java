package com.example.securitystudy.services;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.example.securitystudy.dtos.requests.LoginRequest;
import com.example.securitystudy.dtos.responses.LoginResponse;
import com.example.securitystudy.entities.User;
import com.example.securitystudy.repositories.UserRepository;

@Service
public class LoginServiceImpl implements LoginService{

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtEncoder jwtEncoder;

    public LoginServiceImpl(UserRepository userRepository, 
    BCryptPasswordEncoder bCryptPasswordEncoder,
    JwtEncoder jwtEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = bCryptPasswordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    public LoginResponse authenticate(LoginRequest request) {

        Optional<User> optUser = userRepository.findByUsername(request.username());

        if(optUser.isEmpty() || !passwordEncoder.matches(request.password(), optUser.get().getPassword())){
            throw new BadCredentialsException("User or password is incorrect");
        }
        
        long expiresIn = 300l;
        String token = generateToken(optUser.get(), expiresIn);
    
        return new LoginResponse(token, 300l);
    }
    
    private String generateToken(User user, long expiresIn){
        
        Instant now = Instant.now();

        List<String> roles = user.getRoles().stream()
        .map(role -> role.getRoleName())
        .collect(Collectors.toList());

        JwtEncoderParameters jwtModel = JwtEncoderParameters.from(
                            JwsHeader.with(SignatureAlgorithm.RS256).build(),
                            JwtClaimsSet.builder()
                            .issuer("myAPI")
                            .subject(user.getUserId().toString())
                            .issuedAt(now)
                            .expiresAt(now.plusSeconds(expiresIn))
                            .claim("roles", roles)
                            .build());

        Jwt encodedToken = jwtEncoder.encode(jwtModel);
        String tokenValue = encodedToken.getTokenValue();

        return tokenValue;
    }
}
