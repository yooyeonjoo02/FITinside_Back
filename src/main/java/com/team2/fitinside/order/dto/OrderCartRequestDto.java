package com.team2.fitinside.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCartRequestDto {

    private Long productId; //                                      !!!!필수!!!
    private String productName;
    private int quantity;
    private int itemPrice; // 상품 1개 주문 당시 가격
    private int originalTotalPrice; // 할인 전 총 가격(가격 * 개수)
    private int discountedTotalPrice; // 할인이 적용된 총가격            !!!필수!!!
    private String couponName;
    private Long couponMemberId; // 적용된 쿠폰 ID                      !!!필수!!!

}
