package com.team2.fitinside.banner.controller;

import com.team2.fitinside.banner.dto.BannerResponseDTO;
import com.team2.fitinside.banner.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/banners")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @GetMapping
    public ResponseEntity<List<BannerResponseDTO>> getAllBanners() {
        List<BannerResponseDTO> responses = bannerService.getAllBanners();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BannerResponseDTO> getBannerById(@PathVariable Long id) {
        BannerResponseDTO response = bannerService.getBannerById(id);
        return ResponseEntity.ok(response);
    }
}