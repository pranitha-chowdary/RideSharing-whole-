package com.rideshare.dto.ride;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RideRequest {

    @NotBlank(message = "Pickup location is required")
    private String pickup;

    @NotBlank(message = "Dropoff location is required")
    private String dropoff;

    @NotNull(message = "Pickup latitude is required")
    private BigDecimal pickupLatitude;

    @NotNull(message = "Pickup longitude is required")
    private BigDecimal pickupLongitude;

    @NotNull(message = "Dropoff latitude is required")
    private BigDecimal dropoffLatitude;

    @NotNull(message = "Dropoff longitude is required")
    private BigDecimal dropoffLongitude;

    @NotNull(message = "Scheduled time is required")
    private LocalDateTime scheduledTime;

    @NotNull(message = "Estimated distance is required")
    @DecimalMin(value = "0.1", message = "Distance must be greater than 0")
    private BigDecimal estimatedDistance;

    @NotNull(message = "Estimated duration is required")
    private Integer estimatedDuration;

    @NotNull(message = "Estimated fare is required")
    @DecimalMin(value = "1.0", message = "Fare must be at least 1.0")
    private BigDecimal estimatedFare;
}