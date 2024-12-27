package com.example.securitystudy.services;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.securitystudy.dtos.requests.RegisterRequest;
import com.example.securitystudy.dtos.responses.RegisterResponse;
import com.example.securitystudy.entities.Role;
import com.example.securitystudy.entities.User;
import com.example.securitystudy.repositories.RoleRepository;
import com.example.securitystudy.repositories.UserRepository;
import com.example.securitystudy.security.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class RegisterUserServiceImplTest {

    private User createUser(String username, String password, Role role) {
        User user = new User();
        user.setUserId(UUID.randomUUID());
        user.setUsername(username);
        user.setPassword(password);
        user.setRoles(Set.of(role));
        return user;
    }

    @Test
    @DisplayName("Should create a new User sucessfully")
    public void ShouldCreateANewUserSucessfully(){

        UserRepository userRepository = mock(UserRepository.class);
        RoleRepository roleRepository = mock(RoleRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

        RegisterUserService registerUserService = new RegisterUserServiceImpl(userRepository, 
        roleRepository, 
        passwordEncoder);

        RegisterRequest registerRequest = new RegisterRequest("user1", "123");

        Role userRole = new Role();
        userRole.setRoleId(1l);
        userRole.setRoleName("USER");

        when(roleRepository.findByRoleName(Role.PossibleRoles.USER.name())).thenReturn(userRole);
        when(userRepository.findByUsername(registerRequest.username())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerRequest.password())).thenReturn("hashedPassword");
         
        User savedUser = createUser(registerRequest.username(), 
        "hashedPassword", 
        userRole);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(userCaptor.capture())).thenReturn(savedUser);

        RegisterResponse registerUserResponse = registerUserService.registerUser(registerRequest);
        User capturedUser = userCaptor.getValue();
        
        assertEquals(savedUser.getUsername(), capturedUser.getUsername());
        assertEquals(savedUser.getPassword(), capturedUser.getPassword());
        assertEquals(userRole, capturedUser.getRoles().iterator().next());
        
        assertEquals(savedUser.getUserId(), UUID.fromString(registerUserResponse.userId()));
        assertEquals("Usuario salvo com sucesso!", registerUserResponse.message());

        verify(roleRepository, times(1)).findByRoleName("USER");
        verify(userRepository, times(1)).findByUsername("user1");
        verify(passwordEncoder, times(1)).encode("123");

    }

    @Test
    @DisplayName("Should throw ResponseStatusException when try register an already registered user")
    public void ShouldThrowResponseStatusExceptionWhenTryRegisterAnAlreadyRegisteredUser(){
        // arrange
        UserRepository userRepository = mock(UserRepository.class);
        RoleRepository roleRepository = mock(RoleRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

        RegisterUserService registerUserService = new RegisterUserServiceImpl(userRepository, 
        roleRepository, 
        passwordEncoder);

        RegisterRequest registerRequest = new RegisterRequest("user1", "123");

        Role userRole = new Role();
        userRole.setRoleId(1l);
        userRole.setRoleName("USER");

        User user = createUser("existent user", "123", userRole);

        when(roleRepository.findByRoleName(Role.PossibleRoles.USER.name())).thenReturn(userRole);
        when(userRepository.findByUsername(registerRequest.username())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(registerRequest.password())).thenReturn("hashedPassword");
         
        User savedUser = createUser(registerRequest.username(), 
        "hashedPassword", 
        userRole);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(userCaptor.capture())).thenReturn(savedUser);

        // action
        
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
        () -> registerUserService.registerUser(registerRequest));
        
        // assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getStatusCode());

        verify(roleRepository, times(1)).findByRoleName("USER");
        verify(userRepository, times(1)).findByUsername("user1");
        verify(passwordEncoder, times(0)).encode("123");
        verify(userRepository, times(0)).save(userCaptor.capture());


    }


}
