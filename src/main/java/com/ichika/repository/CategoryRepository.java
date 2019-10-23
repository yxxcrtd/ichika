package com.ichika.repository;

import com.ichika.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {

    List<Category> findByParentIdOrderByIdAsc(Long parentId);

    @Modifying
    @Query("DELETE FROM Category WHERE parentId = ?1")
    void deleteCategoryByParentId(Long parentId);

}
