package javaCA.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import javaCA.model.Category;
import javaCA.service.CategoryService;
import javaCA.validator.CategoryValidator;

@Controller
@RequestMapping("/admin/category")
public class AdminCategoryController {
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private CategoryValidator categoryValidator;
	
	@InitBinder
	private void initCategoryBinder(WebDataBinder binder) {
		binder.addValidators(categoryValidator);
	}

	@GetMapping("/list")
	public String categoryListPage(Model model) {
		List<Category> categoryList = categoryService.findAllCategories();
		model.addAttribute("categoryList", categoryList);

		return "category-list";
	}

	@GetMapping("/edit/{id}")
	public String editCategoryPage(@PathVariable String id, Model model) {
		Category category = categoryService.findCategoryById(id);

		if (category.getCategoryId().equals("annual")) {
			category.setDifferenceForAdmin(category.getDaysOff() - category.getDifferenceForAdmin());
		}

		model.addAttribute("category", category);

		return "category-edit";
	}

	@PostMapping("/edit/{id}")
	public String editCategory(@ModelAttribute @Valid Category category, BindingResult result,
			@PathVariable String id) {
		if (result.hasErrors())
			return "category-edit";

		if (category.getCategoryId().equals("annual")) {
			category.setDifferenceForAdmin(category.getDaysOff() - category.getDifferenceForAdmin());
		}
		
		categoryService.changeCategory(category);
		return "redirect:/admin/category/list";
	}
}
