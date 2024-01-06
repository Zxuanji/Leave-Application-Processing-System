package javaCA.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.ValidationUtils;

import javaCA.model.Employee;

@Component
public class EmployeeValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Employee.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		Employee employee = (Employee) target;
		
		ValidationUtils.rejectIfEmpty(errors, "employeeId", "error.employee.employeeid.empty");
		ValidationUtils.rejectIfEmpty(errors, "name", "error.employee.name.empty");
	}
}
