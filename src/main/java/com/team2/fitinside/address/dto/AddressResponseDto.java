package com.team2.fitinside.address.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressResponseDto {

    private Long addressId;
    private String deliveryReceiver;
    private String deliveryPhone;
    private String postalCode;
    private String deliveryAddress;
    private String detailedAddress;
    private String deliveryMemo;
    private String defaultAddress;
}
