package com.team2.fitinside.banner.service;

import com.team2.fitinside.banner.dto.BannerResponseDTO;
import com.team2.fitinside.banner.entity.Banner;
import com.team2.fitinside.banner.repository.BannerRepository;
import com.team2.fitinside.product.image.S3ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class BannerService {

    private final BannerRepository bannerRepository;
    private final S3ImageService s3ImageService;

    // 배너 생성 로직
    public BannerResponseDTO createBanner(String title, Integer displayOrder, MultipartFile image, String targetUrl) {
        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = s3ImageService.upload(image);
        }

        long maxDisplayOrder = bannerRepository.findByIsDeletedFalse().size() + 1;

        if (displayOrder > maxDisplayOrder) {
            displayOrder = (int) maxDisplayOrder;
        }

        List<Banner> bannersToUpdate = bannerRepository.findByDisplayOrderGreaterThanEqual(displayOrder);
        bannersToUpdate.forEach(b -> b.updateDisplayOrder(b.getDisplayOrder() + 1));
        bannerRepository.saveAll(bannersToUpdate);

        Banner banner = Banner.builder()
                .title(title)
                .displayOrder(displayOrder)
                .imageUrl(imageUrl)
                .targetUrl(targetUrl != null ? targetUrl : "")
                .isDeleted(false)
                .build();

        bannerRepository.save(banner);
        return toResponseDTO(banner);
    }

    // 배너 수정 로직
    public BannerResponseDTO updateBanner(Long id, String title, Integer newDisplayOrder, MultipartFile image, String targetUrl) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Banner not found with id: " + id));

        Integer oldDisplayOrder = banner.getDisplayOrder();
        long maxDisplayOrder = bannerRepository.findByIsDeletedFalse().size();
        if (newDisplayOrder > maxDisplayOrder) {
            newDisplayOrder = (int) maxDisplayOrder;
        }

        if (!oldDisplayOrder.equals(newDisplayOrder)) {
            adjustDisplayOrder(oldDisplayOrder, newDisplayOrder);
        }

        String imageUrl = banner.getImageUrl();

        if (image != null && !image.isEmpty()) {
            if (banner.getImageUrl() != null) {
                s3ImageService.deleteImageFromS3(banner.getImageUrl());
            }
            imageUrl = s3ImageService.upload(image);
        }

        String updatedTargetUrl = targetUrl != null ? targetUrl : banner.getTargetUrl();

        Banner updatedBanner = Banner.builder()
                .id(banner.getId())
                .title(title)
                .displayOrder(newDisplayOrder)
                .imageUrl(imageUrl)
                .targetUrl(updatedTargetUrl)
                .isDeleted(banner.isDeleted())
                .build();

        bannerRepository.save(updatedBanner);
        return toResponseDTO(updatedBanner);
    }

    // 배너 삭제 로직
    public void deleteBanner(Long id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Banner not found with id: " + id));

        Integer currentDisplayOrder = banner.getDisplayOrder();

        List<Banner> bannersToUpdate = bannerRepository.findByDisplayOrderGreaterThan(currentDisplayOrder);
        bannersToUpdate.forEach(b -> b.updateDisplayOrder(b.getDisplayOrder() - 1));
        bannerRepository.saveAll(bannersToUpdate);

        if (banner.getImageUrl() != null) {
            s3ImageService.deleteImageFromS3(banner.getImageUrl());
        }
        bannerRepository.delete(banner);
    }

    private void adjustDisplayOrder(Integer oldOrder, Integer newOrder) {
        if (newOrder > oldOrder) {
            List<Banner> bannersToMoveUp = bannerRepository.findByDisplayOrderBetween(oldOrder + 1, newOrder);
            bannersToMoveUp.forEach(b -> b.updateDisplayOrder(b.getDisplayOrder() - 1));
            bannerRepository.saveAll(bannersToMoveUp);
        } else if (newOrder < oldOrder) {
            List<Banner> bannersToMoveDown = bannerRepository.findByDisplayOrderBetween(newOrder, oldOrder - 1);
            bannersToMoveDown.forEach(b -> b.updateDisplayOrder(b.getDisplayOrder() + 1));
            bannerRepository.saveAll(bannersToMoveDown);
        }
    }

    @Transactional(readOnly = true)
    public List<BannerResponseDTO> getAllBanners() {
        return bannerRepository.findByIsDeletedFalse().stream()
                .sorted(Comparator.comparingInt(Banner::getDisplayOrder))
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BannerResponseDTO getBannerById(Long id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Banner not found with id: " + id));
        return toResponseDTO(banner);
    }

    private BannerResponseDTO toResponseDTO(Banner banner) {
        return BannerResponseDTO.builder()
                .id(banner.getId())
                .title(banner.getTitle())
                .displayOrder(banner.getDisplayOrder())
                .imageUrl(banner.getImageUrl() != null ? banner.getImageUrl() : "")
                .targetUrl(banner.getTargetUrl() != null ? banner.getTargetUrl() : "")
                .build();
    }
}
