package com.team2.fitinside.category.controller;

import com.team2.fitinside.category.dto.CategoryResponseDTO;
import com.team2.fitinside.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // 모든 카테고리 조회
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    // 부모 카테고리 조회
    @GetMapping("/parents")
    public ResponseEntity<List<CategoryResponseDTO>> getParentCategories() {
        return ResponseEntity.ok(categoryService.getParentCategories());
    }

    // 특정 부모의 자식 카테고리 조회
    @GetMapping("/{parentId}/children")
    public ResponseEntity<List<CategoryResponseDTO>> getChildCategories(@PathVariable Long parentId) {
        return ResponseEntity.ok(categoryService.getChildCategories(parentId));
    }

    // 특정 ID의 카테고리 조회
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    // mainDisplayOrder가 설정된 카테고리 조회
    @GetMapping("/mainDisplay")
    public ResponseEntity<List<CategoryResponseDTO>> getMainDisplayCategories() {
        return ResponseEntity.ok(categoryService.getMainDisplayCategories());
    }
}

