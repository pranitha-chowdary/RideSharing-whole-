package com.rideshare.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QRCodeResponse {

    private String qrCodeId;
    private Long rideId;
    private BigDecimal amount;
    private String paymentUrl;
}