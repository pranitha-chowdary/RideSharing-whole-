package com.rideshare.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    @NotBlank(message = "QR code ID is required")
    private String qrCodeId;
    
    @NotBlank(message = "Payment method is required")
    private String paymentMethod;
    
    private Boolean useTokens;
}