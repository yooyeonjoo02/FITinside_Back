package com.team2.fitinside.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryUpdateRequestDTO {
    private Long id;
    private String name;
    private Long displayOrder;
    private Long mainDisplayOrder;
    private Boolean isDeleted;
    private Long parentId;  // 부모 카테고리 id만 참조
    private String imageUrl; // 카테고리 이미지 URL 필드 추가
}