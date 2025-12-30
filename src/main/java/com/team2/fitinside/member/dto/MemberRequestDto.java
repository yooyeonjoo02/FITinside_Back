package com.team2.fitinside.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {

    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "비밀번호가 비어있습니다.")
    @Size(min = 8, message = "비밀번호는 8자 이상 필요합니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).{8,}$",
            message = "비밀번호는 숫자를 포함한 문자 8자 이상 필요합니다.")
    private String password;
    private String userName;

    @Pattern(regexp = "^\\d{10,11}$", message = "전화번호는 10~11자리 입력이 필요합니다.")
    private String phone;


    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
