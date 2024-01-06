package javaCA.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javaCA.model.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {
    @Query("Select c FROM Category c WHERE c.name = :categoryName")
    Category findCategoryByName(@Param("categoryName") String categoryName);
}
