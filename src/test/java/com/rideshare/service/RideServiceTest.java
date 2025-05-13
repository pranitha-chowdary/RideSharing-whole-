package com.rideshare.service;

import com.rideshare.dto.ride.RideRequest;
import com.rideshare.dto.ride.RideResponse;
import com.rideshare.entity.Ride;
import com.rideshare.entity.User;
import com.rideshare.mapper.RideMapper;
import com.rideshare.repository.RideRepository;
import com.rideshare.repository.UserRepository;
import com.rideshare.security.service.UserDetailsImpl;
import com.rideshare.util.RideStatus;
import com.rideshare.util.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RideServiceTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RideMapper rideMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private RideService rideService;

    private User rider;
    private User driver;
    private Ride ride;
    private RideRequest rideRequest;
    private RideResponse rideResponse;
    private UserDetailsImpl riderDetails;
    private UserDetailsImpl driverDetails;

    @BeforeEach
    void setUp() {
        // Set up users
        rider = User.builder()
                .id(1L)
                .username("rider")
                .email("rider@example.com")
                .role(UserRole.RIDER)
                .build();

        driver = User.builder()
                .id(2L)
                .username("driver")
                .email("driver@example.com")
                .role(UserRole.DRIVER)
                .build();

        // Set up ride
        ride = Ride.builder()
                .id(1L)
                .rider(rider)
                .pickup("123 Main St")
                .dropoff("456 Oak Ave")
                .status(RideStatus.REQUESTED)
                .fare(new BigDecimal("25.00"))
                .distance(new BigDecimal("5.0"))
                .createdAt(LocalDateTime.now())
                .build();

        // Set up request
        rideRequest = RideRequest.builder()
                .pickup("123 Main St")
                .dropoff("456 Oak Ave")
                .pickupLatitude(new BigDecimal("37.7749"))
                .pickupLongitude(new BigDecimal("-122.4194"))
                .dropoffLatitude(new BigDecimal("37.7800"))
                .dropoffLongitude(new BigDecimal("-122.4200"))
                .scheduledTime(LocalDateTime.now().plusHours(1))
                .estimatedDistance(new BigDecimal("5.0"))
                .estimatedDuration(15)
                .estimatedFare(new BigDecimal("25.00"))
                .build();

        // Set up response
        rideResponse = RideResponse.builder()
                .id(1L)
                .pickup("123 Main St")
                .dropoff("456 Oak Ave")
                .status(RideStatus.REQUESTED)
                .fare(new BigDecimal("25.00"))
                .distance(new BigDecimal("5.0"))
                .build();

        // Set up user details
        riderDetails = new UserDetailsImpl(
                1L,
                "rider",
                "rider@example.com",
                "Rider Name",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_RIDER")),
                BigDecimal.ZERO
        );

        driverDetails = new UserDetailsImpl(
                2L,
                "driver",
                "driver@example.com",
                "Driver Name",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_DRIVER")),
                BigDecimal.ZERO
        );

        // Mock security context
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void bookRide_WhenUserIsRider_ShouldCreateRide() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(riderDetails);
        when(userRepository.findById(1L)).thenReturn(Optional.of(rider));
        when(rideMapper.rideRequestToRide(rideRequest)).thenReturn(ride);
        when(rideRepository.save(any(Ride.class))).thenReturn(ride);
        when(rideMapper.rideToRideResponse(ride)).thenReturn(rideResponse);

        // Act
        RideResponse result = rideService.bookRide(rideRequest);

        // Assert
        assertNotNull(result);
        assertEquals(RideStatus.REQUESTED, ride.getStatus());
        assertEquals(rider, ride.getRider());
        verify(rideRepository).save(ride);
        verify(rideMapper).rideToRideResponse(ride);
    }

    @Test
    void bookRide_WhenUserIsNotRider_ShouldThrowException() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(driverDetails);
        when(userRepository.findById(2L)).thenReturn(Optional.of(driver));

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> rideService.bookRide(rideRequest));
        verify(rideRepository, never()).save(any());
    }

    @Test
    void acceptRide_WhenUserIsDriver_ShouldUpdateRide() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(driverDetails);
        when(userRepository.findById(2L)).thenReturn(Optional.of(driver));
        when(rideRepository.findById(1L)).thenReturn(Optional.of(ride));
        when(rideRepository.save(any(Ride.class))).thenReturn(ride);
        when(rideMapper.rideToRideResponse(ride)).thenReturn(rideResponse);

        // Act
        RideResponse result = rideService.acceptRide(1L);

        // Assert
        assertNotNull(result);
        assertEquals(RideStatus.ACCEPTED, ride.getStatus());
        assertEquals(driver, ride.getDriver());
        verify(rideRepository).findById(1L);
        verify(rideRepository).save(ride);
        verify(rideMapper).rideToRideResponse(ride);
    }

    @Test
    void acceptRide_WhenRideNotFound_ShouldThrowException() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(driverDetails);
        when(userRepository.findById(2L)).thenReturn(Optional.of(driver));
        when(rideRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> rideService.acceptRide(1L));
        verify(rideRepository, never()).save(any());
    }

    @Test
    void updateRideStatus_WhenInvalidTransition_ShouldThrowException() {
        // Arrange
        Ride completedRide = Ride.builder()
                .id(1L)
                .rider(rider)
                .driver(driver)
                .status(RideStatus.COMPLETED)
                .build();
        
        when(authentication.getPrincipal()).thenReturn(driverDetails);
        when(userRepository.findById(2L)).thenReturn(Optional.of(driver));
        when(rideRepository.findById(1L)).thenReturn(Optional.of(completedRide));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> 
                rideService.updateRideStatus(1L, RideStatus.CANCELLED));
        verify(rideRepository, never()).save(any());
    }
}