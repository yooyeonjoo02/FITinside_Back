package com.team2.fitinside.member.service;

import com.team2.fitinside.global.exception.CustomException;
import com.team2.fitinside.global.exception.ErrorCode;
import com.team2.fitinside.member.dto.MemberRequestDto;
import com.team2.fitinside.member.dto.MemberResponseDto;
import com.team2.fitinside.member.dto.TokenDto;
import com.team2.fitinside.member.entity.Authority;
import com.team2.fitinside.member.entity.Member;
import com.team2.fitinside.jwt.TokenProvider;
import com.team2.fitinside.member.mapper.MemberMapper;
import com.team2.fitinside.oath.repository.RefreshTokenRepository;
import com.team2.fitinside.oath.util.RefreshTokenCookieUtil;
import com.team2.fitinside.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final AuthenticationManagerBuilder managerBuilder;
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenCookieUtil refreshTokenCookieUtil;

    public MemberResponseDto signup(MemberRequestDto requestDto) {
        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE);
        }
        if (memberRepository.findByUserName(requestDto.getUserName()).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE);
        }

        Member member = memberMapper.requestToMember(requestDto);
        member.setAuthority(Authority.ROLE_USER);
        return memberMapper.memberToResponse(memberRepository.save(member));
    }

    public TokenDto login(HttpServletRequest request, HttpServletResponse response, MemberRequestDto requestDto) {

        // 비밀번호 미 입력 시
        if(requestDto.getPassword() == null || requestDto.getPassword().isEmpty()) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        String refreshTokenValue = tokenProvider.generateRefreshToken(authentication);

        Member member = memberRepository.findByEmail(requestDto.getEmail()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        refreshTokenCookieUtil.saveRefreshToken(member.getId(), refreshTokenValue);
        refreshTokenCookieUtil.addRefreshTokenToCookie(request, response, refreshTokenValue); // 리프레시 토큰을 쿠키에 추가

        return tokenDto;
    }

}
