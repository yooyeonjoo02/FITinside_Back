package com.team2.fitinside.address.controller;

import com.team2.fitinside.address.dto.AddressRequestDto;
import com.team2.fitinside.address.dto.AddressResponseDto;
import com.team2.fitinside.address.service.AddressService;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/addresses")
@ApiResponses({
        @ApiResponse(responseCode = "403", description = "권한이 없습니다!", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json"))
})
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    @Operation(summary = "전체 배송지 조회", description = "전체 배송지 조회")
    @ApiResponse(responseCode = "200", description = "전체 배송지 조회 완료", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AddressResponseDto.class))))
    public ResponseEntity<List<AddressResponseDto>> findAllAddresses() {
        List<AddressResponseDto> response = addressService.findAllAddresses();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{address_id}")
    @Operation(summary = "배송지 조회", description = "배송지 조회")
    @ApiResponse(responseCode = "200", description = "배송지 조회 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AddressResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "존재하지 않는 배송지", content = @Content(mediaType = "application/json"))
    public ResponseEntity<AddressResponseDto> findAddress(@PathVariable("address_id") Long addressId) {
        AddressResponseDto response = addressService.findAddress(addressId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/default")
    public ResponseEntity<AddressResponseDto> findDefaultAddress(){
        AddressResponseDto response = addressService.findDefaultAddress();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 새 배송지 정보를 입력하면 무조건 추가
    @PostMapping
    @Operation(summary = "배송지 추가", description = "배송지 추가")
    @ApiResponse(responseCode = "201", description = "배송지 추가 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AddressResponseDto.class)))
    public ResponseEntity<AddressResponseDto> createAddress(@Valid @RequestBody AddressRequestDto request) {
        AddressResponseDto response = addressService.createAddress(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{address_id}")
    @Operation(summary = "배송지 수정", description = "배송지 수정")
    @ApiResponse(responseCode = "200", description = "배송지 수정 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AddressResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "존재하지 않는 배송지", content = @Content(mediaType = "application/json"))
    public ResponseEntity<AddressResponseDto> updateAddress(
            @PathVariable("address_id") Long addressId,
            @Valid @RequestBody AddressRequestDto request) {

        AddressResponseDto response = addressService.updateAddress(addressId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{address_id}")
    @Operation(summary = "배송지 삭제", description = "배송지 삭제")
    @ApiResponse(responseCode = "200", description = "배송지 삭제 완료", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "존재하지 않는 배송지", content = @Content(mediaType = "application/json"))
    public ResponseEntity<String> deleteAddress(@PathVariable("address_id") Long addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.status(HttpStatus.OK).body("배송지 삭제 완료. addressId: " + addressId);
    }

    // 기본 배송지 설정/해제
    @PatchMapping("/{address_id}/default")
    public ResponseEntity<String> checkDefault(
            @PathVariable("address_id") Long addressId,
            @RequestParam("isDefault") String isDefault){
        addressService.checkDefault(addressId, isDefault);
        return ResponseEntity.status(HttpStatus.OK).body("기본 배송지로 등록 완료. addressId: " + addressId);
    }

}
