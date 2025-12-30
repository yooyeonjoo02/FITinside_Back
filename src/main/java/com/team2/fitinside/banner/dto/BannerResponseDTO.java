package com.team2.fitinside.banner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BannerResponseDTO {
    private Long id;
    private String title;
    private String imageUrl;
    private Integer displayOrder;
    private String targetUrl; // URL 필드 추가
    private Boolean isDeleted;
}

