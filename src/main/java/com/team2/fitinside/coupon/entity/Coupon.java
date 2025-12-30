package com.team2.fitinside.coupon.entity;

import com.team2.fitinside.category.entity.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    @Column(name = "coupon_name", nullable = false)
    private String name;

    @Column(name = "coupon_code", length = 6, nullable = false)
    private String code;

    @Column(name = "coupon_type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CouponType type;

    @Column(name = "discount_value")
    @Min(value = 0)
    private int value;

    @Column(name = "discount_percentage")
    @Min(value = 0) @Max(value = 100)
    private int percentage;

    @Column(name = "minimum_purchase_amount")
    @Min(value = 0)
    private int minValue;

    @Column(nullable = false)
    private LocalDate expiredAt;

    @Column(nullable = false)
    private boolean active;

    @Setter     // 연관관계 설정을 위한 세터
    @ManyToOne(fetch = FetchType.LAZY)  // 단방향 다대일 연관관계
    @JoinColumn(name = "category_id")
    private Category category;

    @PrePersist
    public void prePersist() {
        this.active = true; // 엔티티가 저장되기 전에 active를 true로 설정
    }

    public void deActive() {
        this.active = false;
    }
}
