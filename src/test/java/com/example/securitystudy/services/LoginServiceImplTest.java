package com.example.securitystudy.services;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import com.example.securitystudy.dtos.requests.LoginRequest;
import com.example.securitystudy.dtos.responses.LoginResponse;
import com.example.securitystudy.entities.Role;
import com.example.securitystudy.entities.User;
import com.example.securitystudy.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class LoginServiceImplTest {

    private User createUser(String username, String password, String roleName) {
        User user = new User();
        user.setUserId(UUID.randomUUID());
        user.setUsername(username);
        user.setPassword(password);
        Role role = new Role();
        role.setRoleName(roleName);
        user.setRoles(Set.of(role));
        return user;
    }

    @Test
    @DisplayName("Should authenticate User sucessfully and return token")
    public void shouldAuthenticateUserSucessfullyAndReturnToken(){

        UserRepository userRepository = mock(UserRepository.class);
        BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);
        JwtEncoder jwtEncoder = mock(JwtEncoder.class);
        Jwt mockJwt = mock(Jwt.class);

        LoginServiceImpl loginServiceImpl = new LoginServiceImpl(userRepository, 
        passwordEncoder, 
        jwtEncoder);

        LoginRequest loginRequest = new LoginRequest("regular user", 
        "12345");

        User user = createUser("regular user", "hashedPassword", "ROLE_USER");

        when(userRepository.findByUsername(loginRequest.username())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.password(), user.getPassword())).thenReturn(true);
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(mockJwt);
        when(mockJwt.getTokenValue()).thenReturn("mocked_user_token");

        LoginResponse loginResponse = loginServiceImpl.authenticate(loginRequest);

        assertEquals("mocked_user_token", loginResponse.accessToken());

        verify(userRepository, times(1)).findByUsername("regular user");
        verify(passwordEncoder, times(1)).matches(loginRequest.password(), "hashedPassword");
        verify(jwtEncoder, times(1)).encode(any(JwtEncoderParameters.class));
    }

    @Test
    @DisplayName("Should authenticate Admin sucessfully and return token")
    public void shouldAuthenticateAdminSucessfullyAndReturnToken(){

        UserRepository userRepository = mock(UserRepository.class);
        BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);
        JwtEncoder jwtEncoder = mock(JwtEncoder.class);
        Jwt mockJwt = mock(Jwt.class);

        LoginServiceImpl loginServiceImpl = new LoginServiceImpl(userRepository, 
        passwordEncoder, 
        jwtEncoder);

        LoginRequest loginRequest = new LoginRequest("Admin user", 
        "12345");

        User user = createUser("Admin user", "hashedPassword", "ROLE_ADMIN");

        when(userRepository.findByUsername(loginRequest.username())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.password(), user.getPassword())).thenReturn(true);
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(mockJwt);
        when(mockJwt.getTokenValue()).thenReturn("mocked_Admin_token");

        LoginResponse loginResponse = loginServiceImpl.authenticate(loginRequest);

        assertEquals("mocked_Admin_token", loginResponse.accessToken());

        verify(userRepository, times(1)).findByUsername("Admin user");
        verify(passwordEncoder, times(1)).matches(loginRequest.password(), "hashedPassword");
        verify(jwtEncoder, times(1)).encode(any(JwtEncoderParameters.class));
    }

    @Test
    @DisplayName("Should throw BadCredentialsException when username not found")
    public void shouldThrowBadCredentialsExceptionWhenUsernameNotFound(){
        UserRepository userRepository = mock(UserRepository.class);
        BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);
        JwtEncoder jwtEncoder = mock(JwtEncoder.class);

        LoginServiceImpl loginServiceImpl = new LoginServiceImpl(userRepository, 
        passwordEncoder, 
        jwtEncoder);

        LoginRequest loginRequest = new LoginRequest("user2", 
        "12345");

        when(userRepository.findByUsername(loginRequest.username())).thenReturn(Optional.empty());

        BadCredentialsException exception = assertThrows(BadCredentialsException.class,
        () -> loginServiceImpl.authenticate(loginRequest));

        assertEquals("User or password is incorrect", exception.getMessage());

        verify(userRepository, times(1)).findByUsername("user2");
        verify(passwordEncoder, times(0)).matches("", "");
        verify(jwtEncoder, times(0)).encode(any());
    }

    @Test
    @DisplayName("Should throw BadCredentialsException when incorrect password")
    public void shouldThrowBadCredentialsExceptionWhenIncorrectPassword(){
        UserRepository userRepository = mock(UserRepository.class);
        BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);
        JwtEncoder jwtEncoder = mock(JwtEncoder.class);

        LoginServiceImpl loginServiceImpl = new LoginServiceImpl(userRepository, 
        passwordEncoder, 
        jwtEncoder);

        LoginRequest loginRequest = new LoginRequest("user1", 
        "1234");

        User user = createUser("user1", "hashedPassword", "ROLE_USER");

        when(userRepository.findByUsername(loginRequest.username())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.password(), user.getPassword())).thenReturn(false);

        BadCredentialsException exception = assertThrows(BadCredentialsException.class,
        () -> loginServiceImpl.authenticate(loginRequest));

        assertEquals("User or password is incorrect", exception.getMessage());

        verify(userRepository, times(1)).findByUsername("user1");
        verify(passwordEncoder, times(1)).matches("1234", "hashedPassword");
        verify(jwtEncoder, times(0)).encode(any(JwtEncoderParameters.class));
    }
}
