package com.team2.fitinside.config;

import com.team2.fitinside.global.exception.CustomException;
import com.team2.fitinside.global.exception.ErrorCode;
import com.team2.fitinside.member.entity.Member;
import com.team2.fitinside.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SecurityUtil {

    private final MemberRepository memberRepository;

    public Long getCurrentMemberId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Security Context에 인증 정보가 없습니다.");
        }
        authentication.getDetails();

        try {
            // 인증된 이름을 ID로 변환하여 반환
            return Long.parseLong(authentication.getName());
        } catch (NumberFormatException e) {
            // 파싱 실패 시 memberRepository를 사용하여 이름으로 멤버 ID 조회
            Member member = memberRepository.findByUserName(authentication.getName())
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            // 조회된 멤버의 ID 반환
            return member.getId();
        }
    }
}
