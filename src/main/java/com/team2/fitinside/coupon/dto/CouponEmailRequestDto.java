package com.team2.fitinside.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CouponEmailRequestDto {

    private Long couponId;
    private String address;
    private String template;
}
