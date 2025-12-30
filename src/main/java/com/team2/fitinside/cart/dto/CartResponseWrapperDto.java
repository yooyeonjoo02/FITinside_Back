package com.team2.fitinside.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CartResponseWrapperDto {

    private String message;
    private List<CartResponseDto> carts;
}
