package com.team2.fitinside.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    /* 400 BAD_REQUEST : 잘못된 요청 */
    POST_DELETED(HttpStatus.BAD_REQUEST, "삭제된 게시글입니다."),
    REVIEW_DELETED(HttpStatus.BAD_REQUEST, "삭제된 리뷰입니다."),
    COMMENT_DELETED(HttpStatus.BAD_REQUEST, "삭제된 댓글입니다."),
    EMPTY_FILE_EXCEPTION(HttpStatus.BAD_REQUEST, "빈 파일입니다."),
    NO_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "파일 확장자가 없습니다."),
    INVALID_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "허용되지 않는 파일 확장자입니다."),
    INVALID_FILE_FORMAT(HttpStatus.BAD_REQUEST, "허용되지 않는 파일 형식입니다."),
    AUTH_CODE_EXTENSION(HttpStatus.BAD_REQUEST, "로그인을 실패하였습니다(임시)"),
    INVALID_PRODUCT_DATA(HttpStatus.BAD_REQUEST, "상품 정보가 유효하지 않습니다."),
    INVALID_PRODUCT_PRICE(HttpStatus.BAD_REQUEST, "가격은 0이상이어야 합니다."),
    INVALID_PRODUCT_NAME_LENGTH(HttpStatus.BAD_REQUEST, "상품명은 100자 이하로 입력해야 합니다."),
    INVALID_PRODUCT_INFO_LENGTH(HttpStatus.BAD_REQUEST, "상품 설명은 500자 이하로 입력해야 합니다."),
    INVALID_MANUFACTURER_LENGTH(HttpStatus.BAD_REQUEST, "제조사는 100자 이하로 입력해야 합니다."),
    INVALID_COUPON_DATA(HttpStatus.BAD_REQUEST, "쿠폰 정보가 유효하지 않습니다."),
    INVALID_COUPON_CREATE_DATA(HttpStatus.BAD_REQUEST, "쿠폰 생성 정보가 유효하지 않습니다."),
    INVALID_EMAIL_DATA(HttpStatus.BAD_REQUEST, "이메일 정보가 유효하지 않습니다."),
    OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "현재 주문 가능한 상품의 개수를 초과했습니다."),
    ORDER_MODIFICATION_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "배송이 시작된 주문은 수정할 수 없습니다."),
    CART_OUT_OF_RANGE(HttpStatus.BAD_REQUEST, "상품 수량은 1개 이상 20개 이하여야 합니다."),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    INVALID_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "권한 정보가 없는 토큰입니다."),
    USER_NOT_AUTHENTICATED(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다"),

    /* 403 FORBIDDEN : 권한이 없는 사용자 */
    USER_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "권한이 없는 사용자입니다."),

    /* 404 NOT_FOUND : Resource를 찾을 수 없음 */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 정보의 사용자를 찾을 수 없습니다."),
    OBJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "대상을 찾을 수 없습니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "대상을 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 상품을 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 카테고리를 찾을 수 없습니다."),
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 장바구니를 찾을 수 없습니다."),
    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 쿠폰을 찾을 수 없습니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."),
    CART_EMPTY(HttpStatus.NOT_FOUND, "장바구니가 비어있습니다."),
    ORDER_PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 상품에 대한 주문을 찾을 수 없습니다."),
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 배송지를 찾을 수 없습니다."),


    /* 409 : CONFLICT : Resource의 현재 상태와 충돌. 보통 중복된 데이터 존재, 조건을 만족하지 못함 */
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "데이터가 이미 존재합니다."),
    DUPLICATE_COUPON(HttpStatus.CONFLICT, "쿠폰 등록 이력이 존재합니다."),
    EXCEEDED_MAX_ADDRESS_LIMIT(HttpStatus.CONFLICT, "배송지 최대 저장 개수를 초과했습니다."),
    DUPLICATE_ADDRESS(HttpStatus.CONFLICT, "배송지가 이미 존재합니다."),


    /* 410 : GONE : 리소스가 더 이상 유효하지 않음 */
    USER_ALREADY_DELETED(HttpStatus.GONE, "탈퇴된 사용자입니다."),
    PLACE_DELETED(HttpStatus.GONE, "삭제된 장소입니다"),

    /* 500 INTERNAL_SERVER_ERROR : 서버 내부 에러 */
    IO_EXCEPTION_ON_IMAGE_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드 중 입출력 오류가 발생했습니다."),
    PUT_OBJECT_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "S3에 객체를 업로드하는 중 예외가 발생했습니다."),
    IO_EXCEPTION_ON_IMAGE_DELETE(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 삭제 중 입출력 오류가 발생했습니다."),
    PRODUCT_LIST_RETRIEVAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "상품 목록 조회 중 서버 에러 발생!"),
    PRODUCT_DETAIL_RETRIEVAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "상품 상세 조회 중 서버 에러 발생!"),
    PRODUCT_CREATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "상품 등록 중 서버 에러 발생!"),
    PRODUCT_UPDATE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "상품 수정 중 서버 에러 발생!"),
    PRODUCT_DELETION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "상품 삭제 중 서버 에러 발생!"),

    /* 400 BAD_REQUEST : 잘못된 요청 */
    INVALID_CATEGORY_DATA(HttpStatus.BAD_REQUEST, "카테고리 정보가 유효하지 않습니다."),
    /* 410 GONE : 리소스가 삭제된 상태 */
    CATEGORY_ALREADY_DELETED(HttpStatus.GONE, "이미 삭제된 카테고리입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
