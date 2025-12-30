package com.team2.fitinside.banner.controller;

import com.team2.fitinside.banner.dto.BannerResponseDTO;
import com.team2.fitinside.banner.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/banners")
public class BannerAdminController {

    private final BannerService bannerService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BannerResponseDTO> createBanner(
            @RequestParam("title") String title,
            @RequestParam("displayOrder") Integer displayOrder,
            @RequestParam("image") MultipartFile image,
            @RequestParam(value = "targetUrl", required = false) String targetUrl) {

        BannerResponseDTO responseDTO = bannerService.createBanner(title, displayOrder, image, targetUrl);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BannerResponseDTO> updateBanner(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("displayOrder") Integer displayOrder,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "targetUrl", required = false) String targetUrl) {

        BannerResponseDTO responseDTO = bannerService.updateBanner(id, title, displayOrder, image, targetUrl);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBanner(@PathVariable Long id) {
        bannerService.deleteBanner(id);
        return ResponseEntity.noContent().build();
    }
}



