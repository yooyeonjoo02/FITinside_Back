package com.team2.fitinside.category.repository;

import com.team2.fitinside.category.entity.Category;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByIsDeletedFalse();

    Optional<Category> findByIdAndIsDeletedFalse(Long id);

    List<Category> findAllByIsDeletedFalseAndParentIsNullOrderByDisplayOrder();

    List<Category> findAllByIsDeletedFalseAndParentIdOrderByDisplayOrder(Long parentId);

    List<Category> findAllByIsDeletedFalseAndMainDisplayOrderNotNullOrderByMainDisplayOrder();

    Optional<Category> findByNameAndIsDeletedFalse(String name);

    @Modifying
    @Transactional
    @Query("UPDATE Category c SET c.displayOrder = c.displayOrder - 1 WHERE c.displayOrder BETWEEN :startOrder AND :endOrder AND c.parent IS NULL")
    void decrementDisplayOrderForParentCategories(@Param("startOrder") Long startOrder, @Param("endOrder") Long endOrder);

    @Modifying
    @Transactional
    @Query("UPDATE Category c SET c.displayOrder = c.displayOrder + 1 WHERE c.displayOrder BETWEEN :startOrder AND :endOrder AND c.parent IS NULL")
    void incrementDisplayOrderForParentCategories(@Param("startOrder") Long startOrder, @Param("endOrder") Long endOrder);

    @Modifying
    @Transactional
    @Query("UPDATE Category c SET c.displayOrder = c.displayOrder - 1 WHERE c.displayOrder BETWEEN :startOrder AND :endOrder AND c.parent.id = :parentId")
    void decrementDisplayOrderForChildCategories(@Param("startOrder") Long startOrder, @Param("endOrder") Long endOrder, @Param("parentId") Long parentId);

    @Modifying
    @Transactional
    @Query("UPDATE Category c SET c.displayOrder = c.displayOrder + 1 WHERE c.displayOrder BETWEEN :startOrder AND :endOrder AND c.parent.id = :parentId")
    void incrementDisplayOrderForChildCategories(@Param("startOrder") Long startOrder, @Param("endOrder") Long endOrder, @Param("parentId") Long parentId);

    @Modifying
    @Transactional
    @Query("UPDATE Category c SET c.mainDisplayOrder = c.mainDisplayOrder - 1 WHERE c.mainDisplayOrder BETWEEN :startOrder AND :endOrder")
    void decrementMainDisplayOrder(@Param("startOrder") Long startOrder, @Param("endOrder") Long endOrder);

    @Modifying
    @Transactional
    @Query("UPDATE Category c SET c.mainDisplayOrder = c.mainDisplayOrder + 1 WHERE c.mainDisplayOrder BETWEEN :startOrder AND :endOrder")
    void incrementMainDisplayOrder(@Param("startOrder") Long startOrder, @Param("endOrder") Long endOrder);
}

