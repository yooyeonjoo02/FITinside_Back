package com.team2.fitinside.order.controller;

import com.team2.fitinside.order.dto.OrderResponseDto;
import com.team2.fitinside.order.dto.OrderResponseWrapperDto;
import com.team2.fitinside.order.dto.OrderStatusResponseDto;
import com.team2.fitinside.order.dto.OrderStatusUpdateRequestDto;
import com.team2.fitinside.order.service.OrderAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/orders")
@ApiResponses({
        @ApiResponse(responseCode = "403", description = "권한이 없습니다!", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json"))
})
public class OrderAdminController {

    private final OrderAdminService orderAdminService;

    @GetMapping
    @Operation(summary = "관리자의 전체 주문 조회(+주문 상태, 날짜 검색)", description = "전체 주문 조회(+주문 상태, 날짜 검색)")
    @ApiResponse(responseCode = "200", description = "전체 주문 조회 완료", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrderResponseDto.class))))
    public ResponseEntity<OrderResponseWrapperDto> findAllOrdersByAdmin(
            @RequestParam(required = false, value = "page", defaultValue = "1") int page,
            @RequestParam(required = false, value = "orderStatus") String orderStatus,
            @RequestParam(required = false, value = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false, value = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        OrderResponseWrapperDto response = orderAdminService.findAllOrdersByAdmin(page, orderStatus, startDate, endDate);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{order_id}/status")
    @Operation(summary = "관리자의 주문 상태 수정", description = "주문 상태 수정")
    @ApiResponse(responseCode = "200", description = "주문 상태 수정 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "존재하지 않는 주문", content = @Content(mediaType = "application/json"))
    public ResponseEntity<OrderStatusResponseDto> updateStatusOrder(
            @PathVariable("order_id") Long orderId,
            @RequestBody OrderStatusUpdateRequestDto request) {

        OrderStatusResponseDto response = orderAdminService.updateOrderStatus(orderId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{order_id}")
    @Operation(summary = "관리자의 주문 삭제", description = "주문 삭제")
    @ApiResponse(responseCode = "200", description = "주문 삭제 완료", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "존재하지 않는 주문", content = @Content(mediaType = "application/json"))
    public ResponseEntity<String> deleteOrder(@PathVariable("order_id") Long orderId) {
        orderAdminService.deleteOrder(orderId);
        return ResponseEntity.status(HttpStatus.OK).body("주문 삭제 완료. orderId: " + orderId);
    }

}
