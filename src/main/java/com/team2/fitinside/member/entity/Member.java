package com.team2.fitinside.member.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@SQLDelete(sql = "UPDATE member SET is_deleted = true WHERE member_id = ?")
@Where(clause = "is_deleted = false")
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String email;

    private String password;

    @Column(nullable = false)
    private String userName;

    private String phone;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    private boolean isDeleted;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) { this.password = password; }

    public void setPhone(String phone) { this.phone = phone; }

    public void setAuthority(Authority authority) { this.authority = authority; }

    public void setIsDeleted(boolean isDeleted) { this.isDeleted = isDeleted; }


    @Builder
    public Member(Long id, String email, String password, String userName, String phone, Authority authority, boolean isDeleted, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.phone = phone;
        this.authority = authority;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
    }
}