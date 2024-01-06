package javaCA.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import javaCA.model.Role;

@Component
public class RoleValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Role.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		Role role = (Role) target;
		
		ValidationUtils.rejectIfEmpty(errors, "roleId", "error.role.roleid.empty");
		ValidationUtils.rejectIfEmpty(errors, "name", "error.role.name.empty");
	}
}
