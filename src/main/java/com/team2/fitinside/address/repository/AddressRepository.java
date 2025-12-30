package com.team2.fitinside.address.repository;

import com.team2.fitinside.address.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("SELECT a FROM Address a WHERE a.member.id = :memberId " +
            "AND a.isDeleted = false " +
            "ORDER BY a.createdAt DESC")
    List<Address> findAllByMemberId(@Param("memberId") Long memberId);

    Optional<Address> findByIdAndIsDeletedFalse(Long addressId);

    Optional<Address> findByMemberIdAndDefaultAddress(Long memberId, String defaultAddress);
}
