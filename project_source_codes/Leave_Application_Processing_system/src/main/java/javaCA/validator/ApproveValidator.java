package javaCA.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javaCA.model.Approve;
import javaCA.model.LeaveStatusEnum;

@Component
public class ApproveValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Approve.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Approve approve = (Approve) target;
        if (approve.getDecision() == null) {
            errors.rejectValue("decision", "error.decision", "decision must be selected");
        } else {
            if (approve.getDecision().trim().equalsIgnoreCase(LeaveStatusEnum.REJECTED.toString())
                    && approve.getComment().isEmpty()) {
                errors.rejectValue("comment", "error.comment", "comment must be listed");
            }
        }

    }
}
