package com.team2.fitinside.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class OrderUserResponseWrapperDto {

    private List<OrderUserResponseDto> orders;
    private int totalPages;

}
