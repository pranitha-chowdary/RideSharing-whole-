package com.rideshare.service;

import com.rideshare.dto.user.UserProfileDto;
import com.rideshare.dto.user.UserProfileUpdateRequest;
import com.rideshare.entity.User;
import com.rideshare.mapper.UserMapper;
import com.rideshare.repository.UserRepository;
import com.rideshare.security.service.UserDetailsImpl;
import com.rideshare.util.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserDetailsImpl userDetails;
    private UserProfileDto userProfileDto;

    @BeforeEach
    void setUp() {
        // Set up user
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .fullName("Test User")
                .role(UserRole.RIDER)
                .tokenBalance(BigDecimal.TEN)
                .build();

        // Set up user details
        userDetails = new UserDetailsImpl(
                1L,
                "testuser",
                "test@example.com",
                "Test User",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_RIDER")),
                BigDecimal.TEN
        );

        // Set up DTO
        userProfileDto = UserProfileDto.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .fullName("Test User")
                .role(UserRole.RIDER)
                .tokenBalance(BigDecimal.TEN)
                .build();

        // Mock security context
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(userDetails);
    }

    @Test
    void getCurrentUserProfile_ShouldReturnUserProfile() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userMapper.userToUserProfileDto(testUser)).thenReturn(userProfileDto);

        // Act
        UserProfileDto result = userService.getCurrentUserProfile();

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUsername());
        verify(userRepository).findById(1L);
        verify(userMapper).userToUserProfileDto(testUser);
    }

    @Test
    void getCurrentUserProfile_WhenUserNotFound_ShouldThrowException() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userService.getCurrentUserProfile());
        verify(userRepository).findById(1L);
        verify(userMapper, never()).userToUserProfileDto(any());
    }

    @Test
    void updateUserProfile_ShouldUpdateAndReturnProfile() {
        // Arrange
        UserProfileUpdateRequest request = UserProfileUpdateRequest.builder()
                .fullName("Updated Name")
                .email("updated@example.com")
                .phoneNumber("1234567890")
                .build();

        User updatedUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("updated@example.com")
                .fullName("Updated Name")
                .phoneNumber("1234567890")
                .role(UserRole.RIDER)
                .tokenBalance(BigDecimal.TEN)
                .build();

        UserProfileDto updatedDto = UserProfileDto.builder()
                .id(1L)
                .username("testuser")
                .email("updated@example.com")
                .fullName("Updated Name")
                .phoneNumber("1234567890")
                .role(UserRole.RIDER)
                .tokenBalance(BigDecimal.TEN)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(userMapper.userToUserProfileDto(updatedUser)).thenReturn(updatedDto);

        // Act
        UserProfileDto result = userService.updateUserProfile(request);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Name", result.getFullName());
        assertEquals("updated@example.com", result.getEmail());
        assertEquals("1234567890", result.getPhoneNumber());
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
        verify(userMapper).userToUserProfileDto(updatedUser);
    }
}