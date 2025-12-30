package com.team2.fitinside.auth;

import com.team2.fitinside.member.controller.AuthController;
import com.team2.fitinside.member.dto.MemberRequestDto;
import com.team2.fitinside.member.dto.MemberResponseDto;
import com.team2.fitinside.member.dto.TokenDto;
import com.team2.fitinside.member.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private MemberRequestDto requestDtoSignUp;
    private MemberResponseDto responseDtoSignUp;
    private MemberRequestDto requestDtoLogin;
    private TokenDto tokenDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // MemberRequestDto 초기화
        requestDtoSignUp = new MemberRequestDto();
        requestDtoSignUp.setEmail("test@example.com");
        requestDtoSignUp.setPassword("password123");
        requestDtoSignUp.setUserName("tester");
        requestDtoSignUp.setPhone("123-456-7890");

        // MemberResponseDto 초기화
        responseDtoSignUp = new MemberResponseDto();
        responseDtoSignUp.setUserName("tester");
        responseDtoSignUp.setEmail("test@example.com");
        responseDtoSignUp.setPhone("123-456-7890");

        // MemberRequestDto 초기화 (필요에 따라 값 설정)
        requestDtoLogin = new MemberRequestDto();

        // TokenDto 초기화
        tokenDto = new TokenDto();
        tokenDto.setGrantType("Bearer");
        tokenDto.setAccessToken("accessToken123");
        tokenDto.setTokenExpiresIn(3600L); // 1시간 (3600초)
    }


    @Test
    void signup() {
        // given
        when(authService.signup(requestDtoSignUp)).thenReturn(responseDtoSignUp);

        // when
        ResponseEntity<MemberResponseDto> result = authController.signup(requestDtoSignUp);

        // then
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(responseDtoSignUp, result.getBody());

        // verify
        verify(authService, times(1)).signup(requestDtoSignUp);
    }

    @Test
    void login() {
        // given
        when(authService.login(request, response, requestDtoLogin)).thenReturn(tokenDto);

        // when
        ResponseEntity<TokenDto> result = authController.login(requestDtoLogin, response, request);

        // then
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(tokenDto, result.getBody());

        // verify
        verify(authService, times(1)).login(request, response, requestDtoLogin);
    }
}