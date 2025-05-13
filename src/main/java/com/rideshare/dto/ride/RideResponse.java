package com.rideshare.dto.ride;

import com.rideshare.dto.user.UserSummaryDto;
import com.rideshare.util.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RideResponse {

    private Long id;
    private UserSummaryDto rider;
    private UserSummaryDto driver;
    private String pickup;
    private String dropoff;
    private BigDecimal pickupLatitude;
    private BigDecimal pickupLongitude;
    private BigDecimal dropoffLatitude;
    private BigDecimal dropoffLongitude;
    private RideStatus status;
    private BigDecimal fare;
    private BigDecimal distance;
    private Integer estimatedDuration;
    private LocalDateTime scheduledTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createdAt;
    private Boolean isPaid;
    private String qrCodeId;
}