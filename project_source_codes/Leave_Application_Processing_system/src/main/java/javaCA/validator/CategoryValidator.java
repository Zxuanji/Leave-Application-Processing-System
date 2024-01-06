package javaCA.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javaCA.model.Category;

@Component
public class CategoryValidator implements Validator {
	@Override
	public boolean supports(Class<?> clazz) {
		return Category.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		Category category = (Category) target;
		
//		ValidationUtils.rejectIfEmpty(errors, "roleId", "error.role.roleid.empty");
//		ValidationUtils.rejectIfEmpty(errors, "name", "error.role.name.empty");
	}
}
