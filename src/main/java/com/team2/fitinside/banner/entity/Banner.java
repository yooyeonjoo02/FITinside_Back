package com.team2.fitinside.banner.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    private Integer displayOrder;

    private String title;

    // 광고 URL 필드 추가
    private String targetUrl;

    @Builder.Default
    @Column(nullable = false)
    private boolean isDeleted = false;

    // 배너의 정보를 업데이트하는 메서드
    public Banner updateDetails(String title, String imageUrl, Integer displayOrder, String targetUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.displayOrder = displayOrder;
        this.targetUrl = targetUrl; // URL 필드 업데이트
        return this;
    }

    // displayOrder를 변경하는 메서드 (set 대신)
    public void updateDisplayOrder(Integer newDisplayOrder) {
        this.displayOrder = newDisplayOrder;
    }

    // 배너를 삭제 처리하는 메서드
    public void delete() {
        this.isDeleted = true;
    }
}




