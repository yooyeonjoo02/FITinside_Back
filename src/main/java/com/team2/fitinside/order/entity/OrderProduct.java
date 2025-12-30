package com.team2.fitinside.order.entity;

import com.team2.fitinside.coupon.entity.CouponMember;
import com.team2.fitinside.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "order_product")
@SQLDelete(sql = "UPDATE order_product SET is_deleted = true WHERE order_product_id = ?")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "order_product_name", nullable = false)
    private String orderProductName;

    @Column(name = "order_product_price", nullable = false)
    private int orderProductPrice; // 상품 1개의 주문 당시 가격

    @Column(name = "count", nullable = false)
    private int count;

    @OneToOne
    @JoinColumn(name = "coupon_member_id")
    private CouponMember couponMember;

    @Column(name = "discounted_price")
    private int discountedPrice; // (상품가격 * 개수 - 할인)이 적용된 총가격

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    public void setOrder(Order order) {
        this.order = order;
    }

}
