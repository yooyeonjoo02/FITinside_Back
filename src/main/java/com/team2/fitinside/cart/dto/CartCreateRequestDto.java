package com.team2.fitinside.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartCreateRequestDto {

    private Long productId;
    private int quantity;
}
