package com.team2.fitinside.product.image;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/images")
@Tag(name = "Image API", description = "이미지 관련 API")
public class S3ImageController {

    private final S3ImageService s3ImageService;

    @Autowired
    public S3ImageController(S3ImageService s3ImageService) {
        this.s3ImageService = s3ImageService;
    }

    // 이미지 업로드
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "이미지 업로드", description = "S3 버킷에 이미지를 업로드합니다.")
    @ApiResponse(responseCode = "200", description = "이미지 업로드 성공")
    public ResponseEntity<String> uploadImage(@Parameter(description = "업로드할 이미지 파일") @RequestParam("image") MultipartFile image) {
        String imageUrl = s3ImageService.upload(image);
        return ResponseEntity.ok(imageUrl);
    }

    // 이미지 삭제
    @DeleteMapping("/delete")
    @Operation(summary = "이미지 삭제", description = "S3 버킷에서 이미지를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "이미지 삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    public ResponseEntity<?> deleteImage(@RequestParam("imageAddress") String imageAddress) {
        s3ImageService.deleteImageFromS3(imageAddress);
        return ResponseEntity.ok("이미지가 성공적으로 삭제되었습니다.");
    }
}
