package com.team2.fitinside.coupon.dto;

import com.team2.fitinside.coupon.entity.CouponType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class AvailableCouponResponseDto {

    private String name;
    private Long couponMemberId;
    private CouponType type;
    private int value;
    private int percentage;
    private int minValue;
    private LocalDate expiredAt;
}
