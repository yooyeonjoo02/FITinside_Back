package com.team2.fitinside.order.service;

import com.team2.fitinside.global.exception.CustomException;
import com.team2.fitinside.order.entity.OrderStatus;
import com.team2.fitinside.order.dto.*;
import com.team2.fitinside.order.entity.Order;
import com.team2.fitinside.order.mapper.OrderMapper;
import com.team2.fitinside.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.team2.fitinside.global.exception.ErrorCode.ORDER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class OrderAdminService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

    // 전체 주문 조회
    public OrderResponseWrapperDto findAllOrdersByAdmin(int page, String orderStatus, LocalDate startDate, LocalDate endDate) {

        // orderStatus가 null이면 전체 조회
        OrderStatus status = (orderStatus != null && !orderStatus.isEmpty()) ? OrderStatus.valueOf(orderStatus) : null;

        // LocalDate를 LocalDateTime으로 변환
        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = (endDate != null) ? endDate.atTime(23, 59, 59) : null;

        Pageable pageable = PageRequest.of(page-1, 10, Sort.by("createdAt").descending());

        Page<Order> orders = orderRepository.findAllOrdersWithDetails(status, startDateTime, endDateTime, pageable);

        List<OrderResponseDto> orderResponseDtos = orders.stream().map(order -> {
            OrderResponseDto orderResponseDto = orderMapper.toOrderResponseDto(order);

            // 이메일 설정
            orderResponseDto.setEmail(order.getMember().getEmail());

            // 주문의 각 OrderProduct에서 쿠폰 정보를 추출
            List<CouponInfoResponseDto> couponInfoList = order.getOrderProducts().stream()
                    .filter(orderProduct -> orderProduct.getCouponMember() != null)
                    .map(orderProduct -> {
                        CouponInfoResponseDto couponInfo = new CouponInfoResponseDto();
                        couponInfo.setCouponId(orderProduct.getCouponMember().getCoupon().getId());  // Coupon ID 가져오기
                        couponInfo.setName(orderProduct.getCouponMember().getCoupon().getName());  // Coupon 이름 가져오기
                        couponInfo.setDiscountPrice(orderProduct.getOrderProductPrice() * orderProduct.getCount() - orderProduct.getDiscountedPrice());  // 할인 금액 계산
                        return couponInfo;
                    })
                    .collect(Collectors.toList());

            // 쿠폰 정보 리스트 설정
            orderResponseDto.setCoupons(couponInfoList);
            return orderResponseDto;
        }).collect(Collectors.toList());

        return new OrderResponseWrapperDto(orderResponseDtos, orders.getTotalPages());
    }

    // 주문 상태 수정
    @Transactional
    public OrderStatusResponseDto updateOrderStatus(Long orderId, OrderStatusUpdateRequestDto request) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new CustomException(ORDER_NOT_FOUND));

        OrderStatus status = OrderStatus.valueOf(request.getStatus().toUpperCase());
        order.updateOrderStatus(status);
        return orderMapper.toOrderStatusResponseDto(order);
    }

    // 주문 삭제
    @Transactional
    public void deleteOrder(Long orderId) {
        Order findOrder = orderRepository.findById(orderId).orElseThrow(() -> new CustomException(ORDER_NOT_FOUND));
        orderRepository.delete(findOrder);
    }

}
