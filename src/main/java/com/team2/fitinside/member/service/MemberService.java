package com.team2.fitinside.member.service;

import com.team2.fitinside.config.SecurityUtil;
import com.team2.fitinside.global.exception.CustomException;
import com.team2.fitinside.global.exception.ErrorCode;
import com.team2.fitinside.member.dto.MemberListResponse;
import com.team2.fitinside.member.dto.MemberResponseDto;
import com.team2.fitinside.member.entity.Member;
import com.team2.fitinside.member.mapper.MemberMapper;
import com.team2.fitinside.member.repository.MemberRepository;
import com.team2.fitinside.order.entity.Order;
import com.team2.fitinside.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberMapper memberMapper;
    private final SecurityUtil securityUtil;

    public MemberResponseDto getMyInfoBySecurity() {
        Member me = memberRepository.findById(securityUtil.getCurrentMemberId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return memberMapper.memberToResponse(me);
    }

    @Transactional
    public MemberResponseDto changeMemberUserName(String userName) {
        Member member = memberRepository.findById(securityUtil.getCurrentMemberId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (memberRepository.findByUserName(userName).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE);
        }
        else {
            member.setUserName(userName);
        }
        return memberMapper.memberToResponse(memberRepository.save(member));
    }

    @Transactional
    public MemberResponseDto changeMemberPassword(String exPassword, String newPassword) {
        Member member = memberRepository.findById(securityUtil.getCurrentMemberId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(exPassword, member.getPassword())) {
            throw new CustomException(ErrorCode.AUTH_CODE_EXTENSION);
//            throw new RuntimeException("비밀번호가 맞지 않습니다");
        }
        member.setPassword(passwordEncoder.encode((newPassword)));
        return memberMapper.memberToResponse(memberRepository.save(member));
    }

    @Transactional
    public MemberResponseDto changeMemberPhone(String phone) {
        Member member = memberRepository.findById(securityUtil.getCurrentMemberId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        member.setPhone(phone);
        return memberMapper.memberToResponse(memberRepository.save(member));
    }

    @Transactional
    public MemberResponseDto deleteMember(){
        Long memberId = securityUtil.getCurrentMemberId();

        List<Order> orders = orderRepository.findByMemberId(memberId);
        if(!orders.isEmpty()){
            orderRepository.deleteAll(orders);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        memberRepository.delete(member);
        return memberMapper.memberToResponse(member);
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }


    // admin
    public MemberListResponse getMemberList(int page) {

        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by("id").ascending());

        Page<Member> memberList = memberRepository.findAll(pageRequest);

        List<MemberResponseDto> members = new ArrayList<>();

        for(Member member : memberList) {
            members.add(memberMapper.memberToResponse(member));
        }
        // 총 페이지 수
        int totalPages = (memberList.getTotalPages()==0 ? 1 : memberList.getTotalPages());

        return new MemberListResponse(members, totalPages);
    }

    @Transactional
    public void deleteMemberByMemberId(Long memberId) {
        List<Order> orders = orderRepository.findByMemberId(memberId);
        if(!orders.isEmpty()){
            orderRepository.deleteAll(orders);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        memberRepository.delete(member);
    }

    public MemberListResponse getIsDeleteMembers(int page) {
        PageRequest pageRequest = PageRequest.of(page - 1, 10);

        Page<Member> memberList = memberRepository.findAllByIsDeleteTrue(pageRequest);

        List<MemberResponseDto> members = new ArrayList<>();

        for(Member member : memberList) {
            members.add(memberMapper.memberToResponse(member));
        }
        // 총 페이지 수
        int totalPages = (memberList.getTotalPages()==0 ? 1 : memberList.getTotalPages());

        return new MemberListResponse(members, totalPages);
    }
}