package com.team2.fitinside.coupon.entity;

import com.team2.fitinside.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_member_id")
    private Long id;

    @Column(nullable = false)
    private boolean used;

    @ManyToOne(fetch = FetchType.LAZY)  // 단방향 다대일 연관관계
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)  // 단방향 다대일 연관관계
    @JoinColumn(name = "member_id")
    private Member member;

    // 연관관계 설정 편의 메서드
    public void setCouponAndMember(Coupon coupon, Member member) {
        this.coupon = coupon;
        this.member = member;
    }

    public void useCoupon() {
        this.used = true;
    }
}
