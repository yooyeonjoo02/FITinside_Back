package com.team2.fitinside.member.dto;


import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePhoneRequestDto {

    @Pattern(regexp = "^\\d{10,11}$", message = "전화번호는 10~11자리 입력이 필요합니다.")
    private String phone;
}
