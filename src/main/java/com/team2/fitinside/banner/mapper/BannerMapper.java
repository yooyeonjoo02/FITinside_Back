package com.team2.fitinside.banner.mapper;

import com.team2.fitinside.banner.dto.BannerRequestDTO;
import com.team2.fitinside.banner.dto.BannerResponseDTO;
import com.team2.fitinside.banner.entity.Banner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BannerMapper {
    BannerMapper INSTANCE = Mappers.getMapper(BannerMapper.class);

    // DTO -> Entity 변환
    @Mapping(target = "isDeleted", ignore = true)  // isDeleted 필드를 무시
    @Mapping(target = "id", ignore = true)         // id 필드를 무시
    Banner toEntity(BannerRequestDTO requestDTO);
}
