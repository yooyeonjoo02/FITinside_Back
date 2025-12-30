package com.team2.fitinside.member.dto;

import com.team2.fitinside.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MemberListResponse {
    private List<MemberResponseDto> memberList;
    private int totalPages;
}
