package com.team2.fitinside.address.entity;

import com.team2.fitinside.address.dto.AddressRequestDto;
import com.team2.fitinside.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "delivery_receiver", nullable = false)
    private String deliveryReceiver;

    @Column(name = "delivery_phone", nullable = false)
    private String deliveryPhone;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "delivery_address", nullable = false)
    private String deliveryAddress;

    @Column(name = "detailed_address")
    private String detailedAddress;

    @Column(name = "delivery_memo")
    private String deliveryMemo;

    @Column(name = "default_address", nullable = false)
    private String defaultAddress;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    public void setMember(Member member) {
        this.member = member;
    }

    public void deleteAddress() {
        this.isDeleted = true;
    }

    public void updateAddress(AddressRequestDto request) {
        this.deliveryReceiver = request.getDeliveryReceiver();
        this.deliveryPhone = request.getDeliveryPhone();
        this.postalCode = request.getPostalCode();
        this.deliveryAddress = request.getDeliveryAddress();
        this.detailedAddress = request.getDetailedAddress();
        this.deliveryMemo = request.getDeliveryMemo();
        this.defaultAddress = request.getDefaultAddress();
    }

    // 기본 배송지로 설정 여부
    public void checkDefault(String isDefault) {
        if (isDefault.equals("Y")) this.defaultAddress = "Y";
        else this.defaultAddress = "N";
    }

}
