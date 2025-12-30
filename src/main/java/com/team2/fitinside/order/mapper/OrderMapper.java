package com.team2.fitinside.order.mapper;

import com.team2.fitinside.order.dto.*;
import com.team2.fitinside.order.entity.Order;
import com.team2.fitinside.order.entity.OrderProduct;
import com.team2.fitinside.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    // Order -> OrderDetailResponseDto 변환 (주문 생성 후 반환 시 사용)
    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "orderProducts", target = "orderProducts") // 복합 객체나 리스트는 매핑 시 명시적 선언(자동 변환)
//    @Mapping(source = "discountedTotalPrice", target = "discountedTotalPrice")
//    @Mapping(source = "deliveryMemo", target = "deliveryMemo")
    @Mapping(target = "orderStatus", expression = "java(order.getOrderStatus().getDisplayName())") // Enum displayName 매핑
    OrderDetailResponseDto toOrderDetailResponseDto(Order order);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product", target = "productImgUrl", qualifiedByName = "mapFirstDetailImgUrl")
    @Mapping(target = "couponName",
            expression = "java(orderProduct.getCouponMember() != null ? orderProduct.getCouponMember().getCoupon().getName() : null)")
    OrderProductResponseDto toOrderProductResponseDto(OrderProduct orderProduct);

    @Named("mapFirstDetailImgUrl")
    default String mapFirstProductImgUrl(Product product) {
        if (product.getProductImgUrls() != null && !product.getProductImgUrls().isEmpty()) {
            return product.getProductImgUrls().get(0); // 첫 번째 이미지 반환
        }
        return null; // 이미지가 없는 경우 null 반환
    }

    @Mapping(target = "orderStatus", expression = "java(order.getOrderStatus().getDisplayName())")
    OrderStatusResponseDto toOrderStatusResponseDto(Order order);

    // Order -> OrderResponseDto 변환 (주문 상태 변경 시 사용)
    @Mapping(source = "id", target = "orderId")
    OrderResponseDto toOrderResponseDto(Order order);

    // 관리자용 간단한 정보 조회 시 사용
    @Mapping(target = "orderStatus", expression = "java(order.getOrderStatus().getDisplayName())") // Enum displayName 매핑
    List<OrderResponseDto> toOrderResponseDtoList(List<Order> orders);

    // 리스트 타입의 변환 요청이 들어오면 각 요소에 대해 개별 매핑 메서드를 자동으로 호출
    @Mapping(target = "orderStatus", expression = "java(order.getOrderStatus().getDisplayName())") // Enum displayName 매핑
    List<OrderUserResponseDto> toOrderUserResponseDtoList(List<Order> orders);

    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "orderProducts", target = "productNames", qualifiedByName = "mapProductNames")
    @Mapping(source = "orderProducts", target = "productImgUrl", qualifiedByName = "mapFirstProductImgUrl")
    OrderUserResponseDto toOrderUserResponseDto(Order order);

    @Named("mapProductNames")
    default List<String> mapProductNames(List<OrderProduct> orderProducts) {
        return orderProducts.stream()
                .map(orderProduct -> orderProduct.getProduct().getProductName())
                .collect(Collectors.toList());
    }

    @Named("mapFirstProductImgUrl")
    default String mapFirstProductImgUrl(List<OrderProduct> orderProducts) {
        if (orderProducts != null && !orderProducts.isEmpty()) {
            // 첫 번째 상품의 이미지 URL이 존재하면 반환
            List<String> productImgUrls = orderProducts.get(0).getProduct().getProductImgUrls();
            if (productImgUrls != null && !productImgUrls.isEmpty()) {
                return productImgUrls.get(0); // 첫 번째 이미지 URL 반환
            }
        }
        return null; // 이미지가 없으면 null 반환
    }
}
