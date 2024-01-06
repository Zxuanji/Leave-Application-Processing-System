package javaCA.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import javaCA.model.PublicHoliday;

@Component
public class PublicHolidayValidator implements Validator {
	@Override
	public boolean supports(Class<?> clazz) {
		return PublicHoliday.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		PublicHoliday publicHoliday = (PublicHoliday) target;
		if (publicHoliday.getFromDate() != null && publicHoliday.getToDate() != null
				&& publicHoliday.getFromDate().compareTo(publicHoliday.getToDate()) > 0) {
			errors.rejectValue("toDate", "error.dates", "To date should be greater than from date");
		}
		
		ValidationUtils.rejectIfEmpty(errors, "holidayId", "error.holiday.holidayId.empty");
		ValidationUtils.rejectIfEmpty(errors, "name", "error.roholidayle.name.empty");
		ValidationUtils.rejectIfEmpty(errors, "fromDate", "error.holiday.fromdate.empty");
		ValidationUtils.rejectIfEmpty(errors, "toDate", "error.holiday.todate.empty");

	}
}
