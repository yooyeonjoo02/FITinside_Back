package com.team2.fitinside.product.service;

import com.team2.fitinside.product.dto.ProductCreateDto;
import com.team2.fitinside.product.dto.ProductResponseDto;
import com.team2.fitinside.product.dto.ProductUpdateDto;
import com.team2.fitinside.product.entity.Product;
import com.team2.fitinside.global.exception.CustomException;
import com.team2.fitinside.global.exception.ErrorCode;
import com.team2.fitinside.product.image.S3ImageService;
import com.team2.fitinside.product.mapper.ProductMapper;
import com.team2.fitinside.product.repository.ProductRepository;
import com.team2.fitinside.category.repository.CategoryRepository;
import com.team2.fitinside.category.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final S3ImageService s3ImageService;
//    private final String DEFAULT_IMAGE_URL = "https://dummyimage.com/100x100";

    // 페이지네이션, 정렬, 검색을 적용한 상품 전체 목록 조회
    public Page<ProductResponseDto> getAllProducts(int page, int size, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        if (keyword != null && !keyword.isEmpty()) {
            return productRepository.searchByKeywordAndIsDeletedFalse(keyword, pageable)
                    .map(ProductMapper.INSTANCE::toDto);
        } else {
            return productRepository.findByIsDeletedFalse(pageable)
                    .map(ProductMapper.INSTANCE::toDto);
        }
    }

    // 페이지네이션, 정렬, 카테고리 이름을 적용한 상품 목록 조회
    public Page<ProductResponseDto> getAllProductsByCategoryName(int page, int size, String sortField, String sortDir, String categoryName) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        if (categoryName != null && !categoryName.isEmpty()) {
            return productRepository.searchByCategoryNameAndIsDeletedFalse(categoryName, pageable)
                    .map(ProductMapper.INSTANCE::toDto);
        } else {
            return productRepository.findByIsDeletedFalse(pageable)
                    .map(ProductMapper.INSTANCE::toDto);
        }
    }

    // 페이지네이션, 정렬, 검색을 적용한 카테고리별 상품 목록 조회
    public Page<ProductResponseDto> getProductsByCategory(Long categoryId, int page, int size, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        if (keyword != null && !keyword.isEmpty()) {
            return productRepository.searchByKeywordAndCategoryAndIsDeletedFalse(category, keyword, pageable)
                    .map(ProductMapper.INSTANCE::toDto);
        } else {
            return productRepository.findByCategoryAndIsDeletedFalse(category, pageable)
                    .map(ProductMapper.INSTANCE::toDto);
        }
    }

    // 상품 상세 조회
    public ProductResponseDto findProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        return ProductMapper.INSTANCE.toDto(product);
    }

    // 상품 등록 (이미지 업로드 포함)
    @Transactional
    public ProductResponseDto createProduct(ProductCreateDto productCreateDto, List<MultipartFile> productImages, List<MultipartFile> productDescImages) {
        // price 필드의 유효성 검사
        if (productCreateDto.getPrice() < 0) {
            throw new CustomException(ErrorCode.INVALID_PRODUCT_PRICE);
        }

        // productName 필드의 길이 유효성 검사
        if (productCreateDto.getProductName() != null && productCreateDto.getProductName().length() > 100) {
            throw new CustomException(ErrorCode.INVALID_PRODUCT_NAME_LENGTH);
        }

        // info 필드의 길이 유효성 검사
        if (productCreateDto.getInfo() != null && productCreateDto.getInfo().length() > 500) {
            throw new CustomException(ErrorCode.INVALID_PRODUCT_INFO_LENGTH);
        }

        // manufacturer 필드의 길이 유효성 검사
        if (productCreateDto.getManufacturer() != null && productCreateDto.getManufacturer().length() > 100) {
            throw new CustomException(ErrorCode.INVALID_MANUFACTURER_LENGTH);
        }

        Product product = ProductMapper.INSTANCE.toEntity(productCreateDto);

        // categoryName을 통해 categoryId를 조회하는 로직
        Category category = categoryRepository.findByNameAndIsDeletedFalse(productCreateDto.getCategoryName())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
        product.setCategory(category);

        // 상품 이미지 업로드 전 파일 형식 검증
        validateImageTypes(productImages);
        validateImageTypes(productDescImages);

        // S3 상품 이미지 업로드 처리 (이미지 없으면 빈 리스트로 처리)
        List<String> productImageUrls = uploadImages(productImages);

        // 상품 설명 이미지 업로드 처리 (이미지 없으면 빈 리스트로 처리)
        List<String> productDescImageUrls = uploadImages(productDescImages);

        // 상품 이미지 및 설명 이미지 설정
        product.setProductImgUrls(productImageUrls);
        product.setProductDescImgUrls(productDescImageUrls);

        Product savedProduct = productRepository.save(product);

        return ProductMapper.INSTANCE.toDto(savedProduct);
    }


    // 상품 수정 (이미지 업로드 포함)
    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductUpdateDto productUpdateDto,
                                            List<MultipartFile> productImages, List<MultipartFile> productDescImages) {

        // price 필드의 유효성 검사
        if (productUpdateDto.getPrice() < 0) {
            throw new CustomException(ErrorCode.INVALID_PRODUCT_PRICE);
        }

        // productName 필드의 길이 유효성 검사
        if (productUpdateDto.getProductName() != null && productUpdateDto.getProductName().length() > 100) {
            throw new CustomException(ErrorCode.INVALID_PRODUCT_NAME_LENGTH);
        }

        // info 필드의 길이 유효성 검사
        if (productUpdateDto.getInfo() != null && productUpdateDto.getInfo().length() > 500) {
            throw new CustomException(ErrorCode.INVALID_PRODUCT_INFO_LENGTH);
        }

        // manufacturer 필드의 길이 유효성 검사
        if (productUpdateDto.getManufacturer() != null && productUpdateDto.getManufacturer().length() > 100) {
            throw new CustomException(ErrorCode.INVALID_MANUFACTURER_LENGTH);
        }

        // 기존 상품 조회
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        // 업데이트할 상품 정보로 변환
        Product updatedProduct = ProductMapper.INSTANCE.toEntity(id, productUpdateDto);

        // categoryName을 통해 categoryId 조회
        Category category = categoryRepository.findByNameAndIsDeletedFalse(productUpdateDto.getCategoryName())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
        updatedProduct.setCategory(category);

        // 상품 이미지 업로드 전 파일 형식 검증
        validateImageTypes(productImages);
        validateImageTypes(productDescImages);

        // S3 상품 이미지 업데이트 처리 (기존 이미지 유지하면서 새로운 이미지 추가)
        List<String> productImageUrls = new ArrayList<>(existingProduct.getProductImgUrls()); // 기존 이미지 복사
        List<String> newProductImageUrls = uploadImages(productImages); // 새로운 이미지 업로드
        if (!newProductImageUrls.isEmpty()) {
            productImageUrls.addAll(newProductImageUrls); // 새로운 이미지 추가
        }

        // S3 상품 설명 이미지 업데이트 처리 (기존 설명 이미지 유지하면서 새로운 설명 이미지 추가)
        List<String> productDescImageUrls = new ArrayList<>(existingProduct.getProductDescImgUrls()); // 기존 설명 이미지 복사
        List<String> newProductDescImageUrls = uploadImages(productDescImages); // 새로운 설명 이미지 업로드
        if (!newProductDescImageUrls.isEmpty()) {
            productDescImageUrls.addAll(newProductDescImageUrls); // 새로운 설명 이미지 추가
        }

        // 업데이트된 이미지 URL 설정
        updatedProduct.setProductImgUrls(productImageUrls);
        updatedProduct.setProductDescImgUrls(productDescImageUrls);

        // **재고에 따른 품절 여부 수동 설정**
        if (updatedProduct.getStock() == 0) {
            updatedProduct.setIsSoldOut(true); // 재고가 0일 경우 품절로 설정
        } else {
            updatedProduct.setIsSoldOut(false); // 재고가 있을 경우 품절 해제
        }

        // 상품 저장
        Product savedProduct = productRepository.save(updatedProduct);

        // DTO로 변환하여 반환
        return ProductMapper.INSTANCE.toDto(savedProduct);
    }

    // 파일 형식 검증 메서드
    private void validateImageTypes(List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            return; // 이미지가 없을 경우 검증할 필요 없음
        }

        List<String> validImageTypes = Arrays.asList("image/jpeg", "image/png", "image/gif", "image/webp");

        for (MultipartFile image : images) {
            String contentType = image.getContentType();
            if (!validImageTypes.contains(contentType)) {
                throw new CustomException(ErrorCode.INVALID_FILE_FORMAT);
            }
        }
    }



    // 특정 상품 이미지 삭제 로직 (특정 이미지만 삭제)
    @Transactional
    public void deleteProductImages(Long productId, List<String> imageUrlsToDelete) {
        // 기존 상품 조회
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        // S3에서 상품 이미지 삭제 (특정 이미지들만 삭제)
        if (imageUrlsToDelete != null && !imageUrlsToDelete.isEmpty()) {
            for (String imageUrl : imageUrlsToDelete) {
                s3ImageService.deleteImageFromS3(imageUrl); // S3에서 이미지 삭제
            }
            List<String> updatedProductImages = existingProduct.getProductImgUrls();
            updatedProductImages.removeAll(imageUrlsToDelete); // 삭제된 이미지 URL만 제거
            existingProduct.setProductImgUrls(updatedProductImages); // 업데이트된 이미지 리스트 설정
        }

        // 상품 정보 업데이트
        productRepository.save(existingProduct);
    }


    // 특정 상품 설명 이미지 삭제 로직 (특정 설명 이미지만 삭제)
    @Transactional
    public void deleteProductDescriptionImages(Long productId, List<String> descImageUrlsToDelete) {
        // 기존 상품 조회
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        // S3에서 상품 설명 이미지 삭제 (특정 설명 이미지들만 삭제)
        if (descImageUrlsToDelete != null && !descImageUrlsToDelete.isEmpty()) {
            for (String imageUrl : descImageUrlsToDelete) {
                s3ImageService.deleteImageFromS3(imageUrl); // S3에서 설명 이미지 삭제
            }
            List<String> updatedDescImages = existingProduct.getProductDescImgUrls();
            updatedDescImages.removeAll(descImageUrlsToDelete); // 삭제된 설명 이미지 URL만 제거
            existingProduct.setProductDescImgUrls(updatedDescImages); // 업데이트된 설명 이미지 리스트 설정
        }

        // 상품 정보 업데이트
        productRepository.save(existingProduct);
    }




    // 이미지 업로드 처리 메서드 (S3 업로드)
    private List<String> uploadImages(List<MultipartFile> images) {
        List<String> imageUrls = new ArrayList<>();

        // images가 null이 아니고 빈 값이 아닌 경우에만 처리
        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                String imageUrl = s3ImageService.upload(image); // S3에 업로드하고 URL 받기
                imageUrls.add(imageUrl);
            }
        }

        return imageUrls;
    }

    // 상품 삭제 (soft delete)
    @Transactional
    public ProductResponseDto deleteProduct(Long id) {
        Product deletedProduct = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
        deletedProduct.setIsDeleted(true);
        productRepository.save(deletedProduct);
        return ProductMapper.INSTANCE.toDto(deletedProduct);
    }
}
