package com.team2.fitinside.coupon.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CouponMemberResponseDto {

    private String email;
    private String userName;
}
