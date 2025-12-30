package com.team2.fitinside.cart.entity;

import com.team2.fitinside.member.entity.Member;
import com.team2.fitinside.product.entity.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    // (단방향) 다대일 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // (단방향) 다대일 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Min(value = 1) @Max(value = 20)
    @Column(nullable = false)
    private int quantity;

    public void setUserAndProduct(Member member, Product product) {
        this.member = member;
        this.product = product;
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }
}
