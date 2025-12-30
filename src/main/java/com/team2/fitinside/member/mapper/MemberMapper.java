package com.team2.fitinside.member.mapper;

import com.team2.fitinside.member.dto.MemberRequestDto;
import com.team2.fitinside.member.dto.MemberResponseDto;
import com.team2.fitinside.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    @Mapping(target = "password", source = "password", qualifiedByName = "encryptPassword")
    Member requestToMember(MemberRequestDto memberRequestDto);

    MemberResponseDto memberToResponse(Member member);

    List<MemberResponseDto> toDtoList(List<Member> memberList);

    @Named("encryptPassword") // 2
    default String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
}
