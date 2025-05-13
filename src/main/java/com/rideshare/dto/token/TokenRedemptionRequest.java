package com.rideshare.dto.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenRedemptionRequest {

    @NotNull(message = "Amount of tokens to redeem is required")
    @Min(value = 1, message = "Minimum redemption is 1 token")
    private BigDecimal tokensToRedeem;
    
    private Long rideId;
}