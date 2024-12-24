package com.example.securitystudy.services;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.securitystudy.dtos.requests.RegisterRequest;
import com.example.securitystudy.dtos.responses.RegisterResponse;
import com.example.securitystudy.entities.Role;
import com.example.securitystudy.entities.User;
import com.example.securitystudy.repositories.RoleRepository;
import com.example.securitystudy.repositories.UserRepository;

@Service
public class RegisterUserServiceImpl implements RegisterUserService {

    private final UserRepository userRepository;
    
    private final RoleRepository roleRepository;

    public RegisterUserServiceImpl(UserRepository userRepository, 
    RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public RegisterResponse registerUser(RegisterRequest request) {

        Role role = roleRepository.findByRoleName(Role.PossibleRoles.USER.name());
        
        userRepository.findByUsername(request.username()).ifPresent(
            user -> { throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY); });

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(encryptPassword(request.password()));
        user.setRoles(Set.of(role));

        User savedUser = userRepository.save(user);
        
        return new RegisterResponse(
            savedUser.getUserId().toString(), 
            "Usuario salvo com sucesso!");
    }

    private String encryptPassword(String password){
        String salt = BCrypt.gensalt(12);

        return BCrypt.hashpw(password, salt);
    }
    
}
