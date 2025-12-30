package com.team2.fitinside.order.repository;

import com.team2.fitinside.order.entity.OrderStatus;
import com.team2.fitinside.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByMemberId(Long memberId);

    @Query("SELECT DISTINCT o FROM Order o " +
            "JOIN o.orderProducts op " +
            "JOIN op.product p " +
            "WHERE o.member.id = :memberId " +
            "AND o.isDeleted = false " +
            "AND (:productName IS NULL OR p.productName LIKE %:productName%)")
    Page<Order> findByMemberIdAndProductName(@Param("memberId") Long memberId,
                                             @Param("productName") String productName,
                                             Pageable pageable);

    @Query("SELECT o FROM Order o " +
            "JOIN FETCH o.orderProducts op " +
            "LEFT JOIN FETCH op.couponMember cm " +
            "LEFT JOIN FETCH cm.coupon c " +
            "WHERE o.isDeleted = false " +
            "AND (:orderStatus IS NULL OR o.orderStatus = :orderStatus) " +
            "AND (:startDate IS NULL OR o.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR o.createdAt <= :endDate)")
    Page<Order> findAllOrdersWithDetails(@Param("orderStatus") OrderStatus orderStatus,
                                         @Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate,
                                         Pageable pageable);

}
