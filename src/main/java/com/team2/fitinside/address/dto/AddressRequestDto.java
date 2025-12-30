package com.team2.fitinside.address.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequestDto {

    @NotBlank(message = "수령인을 입력해 주세요.")
    private String deliveryReceiver;

    @NotBlank(message = "전화번호를 입력해 주세요.")
    private String deliveryPhone;

    @NotBlank(message = "배송지(우편번호)를 입력해 주세요.")
    private String postalCode;

    @NotBlank(message = "배송지(주소)를 입력해 주세요.")
    private String deliveryAddress;

    private String detailedAddress;

    private String deliveryMemo;

    private String defaultAddress;

}
