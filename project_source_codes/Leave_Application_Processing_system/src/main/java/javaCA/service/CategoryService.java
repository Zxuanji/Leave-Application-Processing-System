package javaCA.service;

import java.util.List;
import java.util.Optional;

import javaCA.model.Category;

public interface CategoryService {
	List<Category> findAllCategories();
	Category findCategoryByName(String categoryName);
	Category findCategoryById(String id);
	Category changeCategory(Category category);
}
