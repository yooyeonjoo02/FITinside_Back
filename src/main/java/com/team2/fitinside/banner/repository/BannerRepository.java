package com.team2.fitinside.banner.repository;

import com.team2.fitinside.banner.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {

    // 삭제되지 않은 배너 목록 가져오기
    List<Banner> findByIsDeletedFalse();

    // 특정 displayOrder보다 크거나 같은 배너들을 조회 (새로운 배너를 삽입할 때 사용)
    List<Banner> findByDisplayOrderGreaterThanEqual(Integer displayOrder);

    // 특정 displayOrder 범위 내에 있는 배너들을 조회 (배너 순서 변경 시 사용)
    List<Banner> findByDisplayOrderBetween(Integer startDisplayOrder, Integer endDisplayOrder);

    // 특정 displayOrder보다 큰 배너들을 조회 (배너 삭제 시 사용)
    List<Banner> findByDisplayOrderGreaterThan(Integer displayOrder);
}

