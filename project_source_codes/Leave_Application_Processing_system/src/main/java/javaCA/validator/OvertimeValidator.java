package javaCA.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import javaCA.model.Overtime;
@Component
public class OvertimeValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Overtime.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        Overtime overtime = (Overtime)obj;
        if ((overtime.getComeTime() != null && overtime.getLeaveTime() != null) && (overtime.getComeTime().compareTo(overtime.getLeaveTime()) > 0)) {
            errors.rejectValue("comeTime", "error.comeTime","End Time must be later than Start Time");
        }

    }
}