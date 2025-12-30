package com.team2.fitinside.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CouponMemberResponseWrapperDto {

    private String message;
    private List<CouponMemberResponseDto> members;

    private int totalPages;
}
