package javaCA.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import javaCA.model.Category;
import javaCA.repository.CategoryRepository;

@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
	@Resource
	private CategoryRepository categoryRepository;

	@Override
	public List<Category> findAllCategories() {
		return categoryRepository.findAll();
	}

	@Override
    public Category findCategoryByName(String categoryName){
        return categoryRepository.findCategoryByName(categoryName);
    }
	
	@Override
	public Category findCategoryById(String id) {
		return categoryRepository.findById(id).orElse(null);
	}
	
	@Override
	@Transactional(readOnly = false)
	public Category changeCategory(Category category) {
		return categoryRepository.saveAndFlush(category);
	}
}
