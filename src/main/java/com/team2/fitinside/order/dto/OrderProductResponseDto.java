package com.team2.fitinside.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProductResponseDto {
    private Long productId;
    private String orderProductName;
    private int orderProductPrice;
    private int count;
    private int discountedPrice; // 할인이 적용된 최종 가격
    private String couponName; // 적용된 쿠폰 이름 (있을 경우)
    private String productImgUrl;
}
