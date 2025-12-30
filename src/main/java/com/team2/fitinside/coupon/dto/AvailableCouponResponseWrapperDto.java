package com.team2.fitinside.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AvailableCouponResponseWrapperDto {

    private String message;
    private List<AvailableCouponResponseDto> coupons;
}
