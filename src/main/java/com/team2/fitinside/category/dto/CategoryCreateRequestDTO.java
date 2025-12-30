package com.team2.fitinside.category.dto;

import lombok.*;
import jakarta.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryCreateRequestDTO {
    @NotBlank(message = "Name is required") // name 필드는 공백일 수 X
    //private Long id;
    private String name;
    private Long displayOrder;
    private Long mainDisplayOrder;
    private Boolean isDeleted;
    private Long parentId;  // 부모 카테고리 id만 참조
    private String imageUrl; // 카테고리 이미지 URL 필드 추가
}

