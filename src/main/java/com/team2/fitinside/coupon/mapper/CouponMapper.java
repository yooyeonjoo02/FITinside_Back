package com.team2.fitinside.coupon.mapper;

import com.team2.fitinside.category.entity.Category;
import com.team2.fitinside.coupon.dto.AvailableCouponResponseDto;
import com.team2.fitinside.coupon.dto.CouponCreateRequestDto;
import com.team2.fitinside.coupon.dto.CouponMemberResponseDto;
import com.team2.fitinside.coupon.dto.CouponResponseDto;
import com.team2.fitinside.coupon.entity.Coupon;
import com.team2.fitinside.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CouponMapper {

    CouponMapper INSTANCE = Mappers.getMapper(CouponMapper.class);

    @Mapping(source = "category", target = "categoryName")
    @Mapping(target = "used", ignore = true)
    CouponResponseDto toCouponResponseDto(Coupon coupon);

    default String mapCategoryToString(Category category) {
        return category != null ? category.getName() : "모든 카테고리";
    }

    @Mapping(target = "couponMemberId", ignore = true)
    AvailableCouponResponseDto toAvailableCouponResponseDto(Coupon coupon);

    CouponMemberResponseDto toCouponMemberResponseDto(Member member);

    @Mapping(target = "category", ignore = true)
    @Mapping(source = "couponCreateRequestDto.name", target = "name")
    @Mapping(source = "code", target = "code")
    Coupon toEntity(CouponCreateRequestDto couponCreateRequestDto, String code);
}
