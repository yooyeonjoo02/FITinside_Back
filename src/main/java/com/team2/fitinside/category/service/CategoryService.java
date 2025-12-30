package com.team2.fitinside.category.service;

import com.team2.fitinside.category.dto.CategoryCreateRequestDTO;
import com.team2.fitinside.category.dto.CategoryResponseDTO;
import com.team2.fitinside.category.dto.CategoryUpdateRequestDTO;
import com.team2.fitinside.category.mapper.CategoryMapper;
import com.team2.fitinside.category.entity.Category;
import com.team2.fitinside.category.repository.CategoryRepository;
import com.team2.fitinside.global.exception.ErrorCode;
import com.team2.fitinside.product.image.S3ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.team2.fitinside.global.exception.CustomException;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final S3ImageService s3ImageService;

    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAllByIsDeletedFalse()
                .stream()
                .map(CategoryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<CategoryResponseDTO> getParentCategories() {
        return categoryRepository.findAllByIsDeletedFalseAndParentIsNullOrderByDisplayOrder()
                .stream()
                .map(CategoryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<CategoryResponseDTO> getChildCategories(Long parentId) {
        return categoryRepository.findAllByIsDeletedFalseAndParentIdOrderByDisplayOrder(parentId)
                .stream()
                .map(CategoryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public CategoryResponseDTO getCategoryById(Long id) {
        Category category = categoryRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
        return CategoryMapper.toResponseDTO(category);
    }

    public List<CategoryResponseDTO> getMainDisplayCategories() {
        return categoryRepository.findAllByIsDeletedFalseAndMainDisplayOrderNotNullOrderByMainDisplayOrder()
                .stream()
                .map(CategoryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    //================================================================
    // 카테고리 생성
    public CategoryCreateRequestDTO createCategory(String name, Long displayOrder, Long mainDisplayOrder, Boolean isDeleted, Long parentId, MultipartFile imageFile) {
        // 부모 카테고리 수에 따라 displayOrder 조정
        long maxDisplayOrder = parentId == null ?
                categoryRepository.findAllByIsDeletedFalseAndParentIsNullOrderByDisplayOrder().size() + 1 :
                categoryRepository.findAllByIsDeletedFalseAndParentIdOrderByDisplayOrder(parentId).size() + 1;

        if (displayOrder > maxDisplayOrder) {
            displayOrder = maxDisplayOrder;
        }

        // 전체 카테고리의 mainDisplayOrder 최대 값 계산
        long maxMainDisplayOrder = categoryRepository.findAllByIsDeletedFalseAndMainDisplayOrderNotNullOrderByMainDisplayOrder().size() + 1;
        if (mainDisplayOrder != null && mainDisplayOrder > maxMainDisplayOrder) {
            mainDisplayOrder = maxMainDisplayOrder;
        }

        // displayOrder와 mainDisplayOrder 조정
        if (parentId == null) {
            categoryRepository.incrementDisplayOrderForParentCategories(displayOrder, Long.MAX_VALUE);
        } else {
            categoryRepository.incrementDisplayOrderForChildCategories(displayOrder, Long.MAX_VALUE, parentId);
        }

        if (mainDisplayOrder != null) {
            adjustMainDisplayOrder(null, mainDisplayOrder);
        }

        String imageUrl = uploadImageToS3(imageFile);
        Category parentCategory = getParentCategory(parentId);

        Category category = Category.builder()
                .name(name)
                .displayOrder(displayOrder)
                .mainDisplayOrder(mainDisplayOrder)
                .parent(parentCategory)
                .isDeleted(isDeleted != null ? isDeleted : false)
                .imageUrl(imageUrl)
                .build();

        return CategoryMapper.toCreateDTO(categoryRepository.save(category));
    }

    // 카테고리 수정
    public CategoryUpdateRequestDTO updateCategory(Long id, String name, Long displayOrder,
                                                   Long mainDisplayOrder, Boolean isDeleted,
                                                   Long parentId, MultipartFile imageFile) {
        Category category = categoryRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        // 부모 카테고리의 최대 displayOrder 값 계산
        Long maxDisplayOrder = category.getParent() == null ?
                (long) categoryRepository.findAllByIsDeletedFalseAndParentIsNullOrderByDisplayOrder().size() :
                (long) categoryRepository.findAllByIsDeletedFalseAndParentIdOrderByDisplayOrder(category.getParent().getId()).size();

        Long oldDisplayOrder = category.getDisplayOrder();
        Long newDisplayOrder = displayOrder;

        // 새로운 displayOrder가 최대치를 넘으면 자동 조정
        if (newDisplayOrder > maxDisplayOrder) {
            newDisplayOrder = maxDisplayOrder;
        }

        // mainDisplayOrder 최대 값 계산
        Long maxMainDisplayOrder = (long) categoryRepository.findAllByIsDeletedFalseAndMainDisplayOrderNotNullOrderByMainDisplayOrder().size();
        Long oldMainDisplayOrder = category.getMainDisplayOrder();
        Long newMainDisplayOrder = mainDisplayOrder;

        if (newMainDisplayOrder != null && newMainDisplayOrder > maxMainDisplayOrder) {
            newMainDisplayOrder = maxMainDisplayOrder;
        }

        // displayOrder와 mainDisplayOrder 값 조정
        if (!oldDisplayOrder.equals(newDisplayOrder)) {
            if (category.getParent() == null) {
                adjustDisplayOrderForParentCategories(oldDisplayOrder, newDisplayOrder);
            } else {
                adjustDisplayOrderForChildCategories(oldDisplayOrder, newDisplayOrder, category.getParent().getId());
            }
        }

        if (!Objects.equals(oldMainDisplayOrder, newMainDisplayOrder)) {
            adjustMainDisplayOrder(oldMainDisplayOrder, newMainDisplayOrder);
        }

        String imageUrl = updateCategoryImage(category, imageFile);

        // Category의 update 메서드를 통해 값 업데이트
        category.updateCategory(name, newDisplayOrder, getParentCategory(parentId), imageUrl, newMainDisplayOrder);

        return CategoryMapper.toUpdateDTO(categoryRepository.save(category));
    }

    //=================================================================
    // 카테고리 삭제
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        if (category.getParent() == null) {
            categoryRepository.decrementDisplayOrderForParentCategories(category.getDisplayOrder(), Long.MAX_VALUE);
        } else {
            categoryRepository.decrementDisplayOrderForChildCategories(category.getDisplayOrder(), Long.MAX_VALUE, category.getParent().getId());
        }

        if (category.getMainDisplayOrder() != null) {
            adjustMainDisplayOrder(category.getMainDisplayOrder(), null);
        }

        category.delete();
    }

    //==================================================================
    private void adjustDisplayOrderForParentCategories(Long oldOrder, Long newOrder) {
        if (newOrder > oldOrder) {
            categoryRepository.decrementDisplayOrderForParentCategories(oldOrder + 1, newOrder);
        } else {
            categoryRepository.incrementDisplayOrderForParentCategories(newOrder, oldOrder - 1);
        }
    }

    private void adjustDisplayOrderForChildCategories(Long oldOrder, Long newOrder, Long parentId) {
        if (newOrder > oldOrder) {
            categoryRepository.decrementDisplayOrderForChildCategories(oldOrder + 1, newOrder, parentId);
        } else {
            categoryRepository.incrementDisplayOrderForChildCategories(newOrder, oldOrder - 1, parentId);
        }
    }

    //================================================================
    private void adjustMainDisplayOrder(Long oldOrder, Long newOrder) {
        if (newOrder == null && oldOrder != null) {
            categoryRepository.decrementMainDisplayOrder(oldOrder, Long.MAX_VALUE);
        } else if (newOrder != null) {
            if (oldOrder == null) {
                categoryRepository.incrementMainDisplayOrder(newOrder, Long.MAX_VALUE);
            } else if (newOrder > oldOrder) {
                categoryRepository.decrementMainDisplayOrder(oldOrder + 1, newOrder); // 범위 재확인
            } else if (newOrder < oldOrder) {
                categoryRepository.incrementMainDisplayOrder(newOrder, oldOrder - 1);
            }
        }
    }

    private Category getParentCategory(Long parentId) {
        return (parentId != null) ? categoryRepository.findByIdAndIsDeletedFalse(parentId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND)) : null;
    }

    private String uploadImageToS3(MultipartFile imageFile) {
        return (imageFile != null && !imageFile.isEmpty()) ? s3ImageService.upload(imageFile) : null;
    }

    private String updateCategoryImage(Category category, MultipartFile imageFile) {
        String imageUrl = category.getImageUrl();
        if (imageFile != null && !imageFile.isEmpty()) {
            if (imageUrl != null) {
                s3ImageService.deleteImageFromS3(imageUrl);
            }
            imageUrl = s3ImageService.upload(imageFile);
        }
        return imageUrl;
    }
}
