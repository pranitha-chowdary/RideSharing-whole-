package com.rideshare.dto.user;

import com.rideshare.util.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {

    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String address;
    private String profilePicture;
    private UserRole role;
    private BigDecimal tokenBalance;
    private Boolean accountVerified;
}