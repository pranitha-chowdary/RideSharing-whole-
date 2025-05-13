package com.rideshare.dto.token;

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
public class TokenResponse {

    private Long id;
    private Long userId;
    private Long rideId;
    private BigDecimal tokensEarned;
    private Boolean redeemed;
    private LocalDateTime createdAt;
    private LocalDateTime redemptionDate;
    private BigDecimal redemptionValue;
}