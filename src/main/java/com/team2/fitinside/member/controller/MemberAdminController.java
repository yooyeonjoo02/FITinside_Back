package com.team2.fitinside.member.controller;


import com.team2.fitinside.member.dto.MemberListResponse;
import com.team2.fitinside.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/member")
public class MemberAdminController {

    private final MemberService memberService;

    @GetMapping("")
    public ResponseEntity<MemberListResponse> getMembers(
            @RequestParam(required = false, value = "page", defaultValue = "1") int page) {
        MemberListResponse memberList = memberService.getMemberList(page);

        return ResponseEntity.ok(memberList);
    }

    @GetMapping("/delete")
    public ResponseEntity<MemberListResponse> getDeleteMember(
            @RequestParam(required = false, value = "page", defaultValue = "1") int page) {
        MemberListResponse memberList = memberService.getIsDeleteMembers(page);

        return ResponseEntity.ok(memberList);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<?> deleteMember(@PathVariable("memberId") Long memberId) {
        memberService.deleteMemberByMemberId(memberId);
        return ResponseEntity.status(HttpStatus.OK).body("회원 정지 완료 : " + memberId);
    }
}
