package com.team2.fitinside.member.repository;

import com.team2.fitinside.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<Member> findByUserName(String userName);

    Page<Member> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM member WHERE is_deleted = true", nativeQuery = true)
    Page<Member> findAllByIsDeleteTrue(Pageable pageable);

}
