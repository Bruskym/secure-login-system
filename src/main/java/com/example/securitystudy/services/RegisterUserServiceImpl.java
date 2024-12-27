package com.example.securitystudy.services;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.securitystudy.dtos.requests.RegisterRequest;
import com.example.securitystudy.dtos.responses.RegisterResponse;
import com.example.securitystudy.entities.Role;
import com.example.securitystudy.entities.User;
import com.example.securitystudy.repositories.RoleRepository;
import com.example.securitystudy.repositories.UserRepository;
import com.example.securitystudy.security.PasswordEncoder;

@Service
public class RegisterUserServiceImpl implements RegisterUserService {

    private final UserRepository userRepository;
    
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public RegisterUserServiceImpl(UserRepository userRepository, 
    RoleRepository roleRepository,
    PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public RegisterResponse registerUser(RegisterRequest request) {

        Role role = roleRepository.findByRoleName(Role.PossibleRoles.USER.name());
        
        userRepository.findByUsername(request.username()).ifPresent(
            user -> { throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY); });

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRoles(Set.of(role));

        User savedUser = userRepository.save(user);
        
        return new RegisterResponse(
            savedUser.getUserId().toString(), 
            "Usuario salvo com sucesso!");
    }
}
