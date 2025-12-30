package com.team2.fitinside.order.controller;

import com.team2.fitinside.order.dto.OrderDetailResponseDto;
import com.team2.fitinside.order.dto.OrderRequestDto;
import com.team2.fitinside.order.dto.OrderUserResponseDto;
import com.team2.fitinside.order.dto.OrderUserResponseWrapperDto;
import com.team2.fitinside.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@ApiResponses({
        @ApiResponse(responseCode = "403", description = "권한이 없습니다!", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json"))
})
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/orders/{order_id}")
    @Operation(summary = "로그인한 회원의 상세 주문 조회", description = "상세 주문 조회")
    @ApiResponse(responseCode = "200", description = "상세 주문 조회 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDetailResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "존재하지 않는 주문", content = @Content(mediaType = "application/json"))
    public ResponseEntity<OrderDetailResponseDto> findOrder(@PathVariable("order_id") Long orderId) {
        OrderDetailResponseDto response = orderService.findOrder(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/orders")
    @Operation(summary = "로그인한 회원의 전체 주문 조회(+상품 이름 검색)", description = "전체 주문 조회(+상품 이름 검색)")
    @ApiResponse(responseCode = "200", description = "전체(검색) 주문 조회 완료", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrderUserResponseDto.class))))
    public ResponseEntity<OrderUserResponseWrapperDto> findAllOrders(
            @RequestParam(required = false, value = "page", defaultValue = "1") int page,
            @RequestParam(required = false, value = "productName") String productName) {

        OrderUserResponseWrapperDto response = orderService.findAllOrders(page, productName);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/order")
    @Operation(summary = "로그인한 회원의 주문 생성", description = "주문 생성")
    @ApiResponse(responseCode = "201", description = "주문 생성 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDetailResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "품절된 상품", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "비어있는 장바구니", content = @Content(mediaType = "application/json"))
    public ResponseEntity<OrderDetailResponseDto> createOrder(@Valid @RequestBody OrderRequestDto request) {
        OrderDetailResponseDto response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/orders/{order_id}")
    @Operation(summary = "로그인한 회원의 주문 수정", description = "주문 수정")
    @ApiResponse(responseCode = "200", description = "주문 수정 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDetailResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "배송이 시작(완료)된 상품은 수정 불가", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "존재하지 않는 주문", content = @Content(mediaType = "application/json"))
    public ResponseEntity<OrderDetailResponseDto> updateOrder(
            @PathVariable("order_id") Long orderId,
            @Valid @RequestBody OrderRequestDto request) {

        OrderDetailResponseDto response = orderService.updateOrder(orderId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/orders/{order_id}")
    @Operation(summary = "로그인한 회원의 주문 취소", description = "주문 취소")
    @ApiResponse(responseCode = "200", description = "주문 취소 완료", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "배송이 시작(완료)된 상품은 취소 불가", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "존재하지 않는 주문", content = @Content(mediaType = "application/json"))
    public ResponseEntity<String> cancelOrder(@PathVariable("order_id") Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.status(HttpStatus.OK).body("주문 취소 완료. orderId: " + orderId);
    }

}
