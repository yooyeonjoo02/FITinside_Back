package com.team2.fitinside.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CouponInfoResponseDto {

    private Long couponId;
    private String name;
    private int discountPrice;

}
