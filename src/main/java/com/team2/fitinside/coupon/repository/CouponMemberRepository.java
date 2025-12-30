package com.team2.fitinside.coupon.repository;

import com.team2.fitinside.coupon.entity.CouponMember;
import com.team2.fitinside.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CouponMemberRepository extends JpaRepository<CouponMember, Long> {

    @Query("SELECT cm " +
            "FROM CouponMember cm " +
            "JOIN FETCH cm.member " +
            "WHERE cm.coupon.id = :couponId")
    Page<CouponMember> findByCoupon_Id(@Param("couponId") Long couponId, Pageable pageable);

    @Query("SELECT cm FROM CouponMember cm " +
            "JOIN FETCH cm.coupon c " +
            "LEFT JOIN FETCH c.category " +
            "WHERE cm.member.id = :memberId " +
            "ORDER BY c.expiredAt ASC")
    Page<CouponMember> findByMemberIdWithCouponsAndCategories(@Param("memberId") Long memberId, Pageable pageable);

    @Query("SELECT cm FROM CouponMember cm " +
            "JOIN FETCH cm.coupon c " +
            "LEFT JOIN FETCH c.category " +
            "WHERE cm.member.id = :memberId AND c.active = :active AND cm.used = :used " +
            "ORDER BY c.expiredAt ASC")
    Page<CouponMember> findByMemberIdAndCouponActiveAndUsed(@Param("memberId") Long memberId,
                                                            @Param("active") boolean active,
                                                            @Param("used") boolean used,
                                                            Pageable pageable);


    @Query("SELECT cm FROM CouponMember cm " +
            "JOIN FETCH cm.coupon c " +
            "LEFT JOIN FETCH c.category " + // 카테고리가 없는 경우도 고려
            "WHERE cm.member.id = :memberId AND " +
            "(c.category.id = :categoryId OR c.category IS NULL)")
    List<CouponMember> findByMember_IdAndCoupon_Category_Id(@Param("memberId") Long memberId,
                                                            @Param("categoryId") Long categoryId);

    boolean existsByCoupon_CodeAndMember_Id(String code, Long memberId);

    Optional<CouponMember> findByMember_IdAndCoupon_IdAndUsedIs(Long memberId, Long couponId, boolean used);

    @Query("SELECT m " +
            "FROM Member m " +
            "WHERE m.id NOT IN " +
                "(SELECT cm2.member.id " +
                "FROM CouponMember cm2 " +
                "WHERE cm2.coupon.id = :couponId)")
    List<Member> findCouponMembersWithoutCoupons(@Param("couponId") Long couponId);

    @Query("SELECT cm FROM CouponMember cm " +
            "JOIN FETCH cm.coupon c " +
            "WHERE cm.member.id = :memberId AND c.name LIKE %:couponName%")
    List<CouponMember> findByMember_IdAndCoupon_Name_Contains(@Param("memberId") Long memberId, @Param("couponName") String couponName);
}
