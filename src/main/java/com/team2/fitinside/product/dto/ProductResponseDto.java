package com.team2.fitinside.product.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ProductResponseDto {

    private Long id;                 // 상품 ID
    private Long categoryId;         // 카테고리 ID
    private String categoryName;     // 카테고리 이름
    private String productName;      // 상품명
    private int price;               // 가격
    private String info;             // 상품 설명
    private int stock;               // 재고
    private boolean isSoldOut;       // 품절여부
    private String manufacturer;     // 재조사
    private List<String> productImgUrls;     //이미지url들
    private List<String> productDescImgUrls; // 상품 설명 이미지들


    private boolean isDeleted;       // 삭제 상태
    private LocalDateTime createdAt; // 생성 시간
    private LocalDateTime updatedAt; // 수정 시간

}
