package com.mystreet.service;

import com.mystreet.dto.AuthRequest;
import com.mystreet.dto.AuthResponse;
import com.mystreet.dto.RegisterRequest;
import com.mystreet.exception.BadRequestException;
import com.mystreet.exception.UnauthorizedException;
import com.mystreet.model.User;
import com.mystreet.repository.UserRepository;
import com.mystreet.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private AuthRequest authRequest;
    private User user;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setFirstName("Test");
        registerRequest.setLastName("User");

        authRequest = new AuthRequest();
        authRequest.setEmail("test@example.com");
        authRequest.setPassword("password123");

        user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("test@example.com");
        user.setPasswordHash("hashedPassword");
        user.setIsAdmin(false);
    }

    @Test
    void register_Success() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtUtil.generateToken(any(UUID.class), anyString(), any(Boolean.class)))
                .thenReturn("token123");

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("token123", response.getToken());
        assertEquals("test@example.com", response.getEmail());
        assertFalse(response.getIsAdmin());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_EmailAlreadyExists_ThrowsException() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> authService.register(registerRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_Success() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(any(UUID.class), anyString(), any(Boolean.class)))
                .thenReturn("token123");

        AuthResponse response = authService.login(authRequest);

        assertNotNull(response);
        assertEquals("token123", response.getToken());
        assertEquals("test@example.com", response.getEmail());
    }

    @Test
    void login_UserNotFound_ThrowsException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class, () -> authService.login(authRequest));
    }

    @Test
    void login_InvalidPassword_ThrowsException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> authService.login(authRequest));
    }
}
