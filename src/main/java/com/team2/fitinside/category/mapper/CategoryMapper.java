package com.team2.fitinside.category.mapper;

import com.team2.fitinside.category.dto.CategoryCreateRequestDTO;
import com.team2.fitinside.category.dto.CategoryUpdateRequestDTO;
import com.team2.fitinside.category.dto.CategoryResponseDTO;
import com.team2.fitinside.category.entity.Category;

public class CategoryMapper {

    // Category -> CategoryCreateRequestDTO 변환
    public static CategoryCreateRequestDTO toCreateDTO(Category category) {
        if (category == null) {
            return null;
        }

        return CategoryCreateRequestDTO.builder()
                .name(category.getName())
                .displayOrder(category.getDisplayOrder())
                .mainDisplayOrder(category.getMainDisplayOrder())
                .isDeleted(category.getIsDeleted())
                .parentId(getParentId(category))
                .imageUrl(category.getImageUrl()) // 이미지 URL 추가
                .build();
    }

    // Category -> CategoryUpdateRequestDTO 변환
    public static CategoryUpdateRequestDTO toUpdateDTO(Category category) {
        if (category == null) {
            return null;
        }

        return CategoryUpdateRequestDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .displayOrder(category.getDisplayOrder())
                .mainDisplayOrder(category.getMainDisplayOrder())
                .isDeleted(category.getIsDeleted())
                .parentId(getParentId(category))
                .imageUrl(category.getImageUrl()) // 이미지 URL 추가
                .build();
    }

    // Category -> CategoryResponseDTO 변환
    public static CategoryResponseDTO toResponseDTO(Category category) {
        if (category == null) {
            return null;
        }

        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .displayOrder(category.getDisplayOrder())
                .mainDisplayOrder(category.getMainDisplayOrder())
                .isDeleted(category.getIsDeleted())
                .parentId(getParentId(category))
                .imageUrl(category.getImageUrl()) // 이미지 URL 추가
                .build();
    }

    // DTO -> Category 변환 (Create용)
    public static Category toEntityFromCreateDTO(CategoryCreateRequestDTO dto, Category parentCategory) {
        if (dto == null) {
            return null;
        }

        return Category.builder()
                .name(dto.getName())
                .displayOrder(dto.getDisplayOrder())
                .mainDisplayOrder(dto.getMainDisplayOrder())
                .isDeleted(dto.getIsDeleted())
                .parent(parentCategory)
                .imageUrl(dto.getImageUrl()) // 이미지 URL 추가
                .build();
    }

    // DTO -> Category 변환 (Update용)
    public static Category toEntityFromUpdateDTO(CategoryUpdateRequestDTO dto, Category parentCategory) {
        if (dto == null) {
            return null;
        }

        return Category.builder()
                .id(dto.getId())
                .name(dto.getName())
                .displayOrder(dto.getDisplayOrder())
                .isDeleted(dto.getIsDeleted())
                .mainDisplayOrder(dto.getMainDisplayOrder())
                .parent(parentCategory)
                .imageUrl(dto.getImageUrl()) // 이미지 URL 추가
                .build();
    }

    // 부모 카테고리 ID를 반환하는 헬퍼 메서드
    private static Long getParentId(Category category) {
        return category.getParent() != null ? category.getParent().getId() : null;
    }
}
