package com.team2.fitinside.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderUserResponseDto { // 주문 확인 DTO (회원 목록용)

    private Long orderId;
    private String orderStatus;
    private int totalPrice;
    private int discountedTotalPrice;
    private String deliveryAddress;
    private List<String> productNames; // 주문 상품 이름 목록
    private String productImgUrl; // 첫 번째 상품의 첫번째 이미지
    private LocalDateTime createdAt;

}
