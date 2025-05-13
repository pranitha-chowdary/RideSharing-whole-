package com.rideshare.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rideshare.dto.auth.JwtResponse;
import com.rideshare.dto.auth.LoginRequest;
import com.rideshare.dto.auth.SignupRequest;
import com.rideshare.entity.User;
import com.rideshare.service.AuthService;
import com.rideshare.util.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    private LoginRequest loginRequest;
    private SignupRequest signupRequest;
    private JwtResponse jwtResponse;
    private User user;

    @BeforeEach
    void setUp() {
        loginRequest = LoginRequest.builder()
                .username("testuser")
                .password("password")
                .build();

        signupRequest = SignupRequest.builder()
                .username("newuser")
                .email("newuser@example.com")
                .password("password")
                .role(UserRole.RIDER)
                .fullName("New User")
                .phoneNumber("1234567890")
                .build();

        jwtResponse = JwtResponse.builder()
                .token("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .role(UserRole.RIDER)
                .build();

        user = User.builder()
                .id(1L)
                .username("newuser")
                .email("newuser@example.com")
                .password("encoded_password")
                .role(UserRole.RIDER)
                .build();
    }

    @Test
    void authenticateUser_ShouldReturnJwtToken() throws Exception {
        when(authService.authenticateUser(any(LoginRequest.class))).thenReturn(jwtResponse);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.role").value("RIDER"));
    }

    @Test
    void registerUser_ShouldReturnSuccess() throws Exception {
        when(authService.registerUser(any(SignupRequest.class))).thenReturn(user);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User registered successfully with username: newuser"));
    }

    @Test
    void registerUser_WhenUsernameExists_ShouldReturnError() throws Exception {
        when(authService.registerUser(any(SignupRequest.class)))
                .thenThrow(new RuntimeException("Username is already taken"));

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Username is already taken"));
    }
}