package com.team2.fitinside.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartResponseDto {

    private Long productId;
    private int quantity;
}
