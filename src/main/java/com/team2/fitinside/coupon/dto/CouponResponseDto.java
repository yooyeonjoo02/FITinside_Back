package com.team2.fitinside.coupon.dto;

import com.team2.fitinside.coupon.entity.CouponType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CouponResponseDto {

    private Long id;
    private String name;
    private String code;
    private CouponType type;
    private int value;
    private int percentage;
    private int minValue;
    private boolean active;
    private LocalDate expiredAt;
    private String categoryName;
    private boolean used;
}
