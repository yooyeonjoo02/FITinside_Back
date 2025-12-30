package com.team2.fitinside.oath.util;

import com.team2.fitinside.oath.entity.RefreshToken;
import com.team2.fitinside.oath.repository.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.team2.fitinside.jwt.TokenProvider.REFRESH_TOKEN_EXPIRE_TIME;

@RequiredArgsConstructor
@Component
public class RefreshTokenCookieUtil {

    private final RefreshTokenRepository refreshTokenRepository;

    // 리프레시 토큰을 DB에 저장하는 메서드
    public void saveRefreshToken(Long userId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByMemberId(userId)
                .map(entity -> entity.update(newRefreshToken))      // 기존 토큰이 있으면 업데이트
                .orElse(new RefreshToken(userId, newRefreshToken)); // 없으면 새로 생성

        refreshTokenRepository.save(refreshToken); // 저장소에 리프레시 토큰 저장
    }

    // 리프레시 토큰을 쿠키에 추가하는 메서드
    public void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) REFRESH_TOKEN_EXPIRE_TIME; // 쿠키 유효 기간 설정

        CookieUtil.deleteCookie(request, response, "refreshToken"); // 기존 쿠키 삭제
        CookieUtil.addCookie(response, "refreshToken", refreshToken, cookieMaxAge); // 새 쿠키 추가
    }
}
