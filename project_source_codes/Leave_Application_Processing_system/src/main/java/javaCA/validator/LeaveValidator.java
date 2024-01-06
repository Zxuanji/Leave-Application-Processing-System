package javaCA.validator;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javaCA.model.Leave;
import javaCA.model.LeaveStatusEnum;
import javaCA.model.PublicHoliday;
import javaCA.model.Role;
import javaCA.model.User;
import javaCA.service.CategoryService;
import javaCA.service.LeaveService;
import javaCA.service.OvertimeCalculateService;
import javaCA.service.PublicHolidayService;
import javaCA.service.UserService;

@Component
public class LeaveValidator implements Validator {
    
    @Autowired
    LeaveService leaveService;
    @Autowired
    PublicHolidayService phService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    UserService userService;
    @Autowired
    OvertimeCalculateService overtimeCalculateService;

    @Override
    public boolean supports(Class<?> clazz){
        return Leave.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors){
        Leave leave = (Leave) obj;
        String employeeId = leave.getEmployeeId();
        User user = userService.findUserByEmployeeId(employeeId);
        List<Role> roles = user.getRoleSet();
        LocalDate fromDate = leave.getFromDate();
        LocalDate toDate = leave.getToDate();


        if (fromDate != null){
            if (fromDate.getDayOfWeek() == DayOfWeek.SATURDAY || fromDate.getDayOfWeek() == DayOfWeek.SUNDAY){
                errors.rejectValue("fromDate", "error.fromDate", "Start Date must be a working day");
            }
            for (PublicHoliday ph: phService.findAllHolidays()){
                if(fromDate.isAfter(ph.getFromDate()) && fromDate.isBefore(ph.getToDate()) || 
                    fromDate.isEqual(ph.getFromDate()) || fromDate.isEqual(ph.getToDate())){
                    errors.rejectValue("fromDate", "error.fromDate", "Start Date must be a working day");
                }
            }
        }
        if (toDate != null){
            if (toDate.getDayOfWeek() == DayOfWeek.SATURDAY || toDate.getDayOfWeek() == DayOfWeek.SUNDAY){
                errors.rejectValue("toDate", "error.toDate", "End Date must be a working day");
            }
            for (PublicHoliday ph: phService.findAllHolidays()){
                if(toDate.isAfter(ph.getFromDate()) && toDate.isBefore(ph.getToDate()) || 
                    toDate.isEqual(ph.getFromDate()) || toDate.isEqual(ph.getToDate())){
                    errors.rejectValue("toDate", "error.toDate", "End Date must be a working day");
                }
            }
        }

        if (fromDate != null && toDate != null){          
            if (toDate.isBefore(fromDate)){
                errors.rejectValue("toDate", "error.toDate", "End Date must be on or after Start Date");
            }
        }

        if (leave.getCategory() == null){
            errors.rejectValue("category", "error.category", "Category is required");
        }
        else{
            if (fromDate != null && toDate != null){
                long leaveDays = fromDate.until(toDate, ChronoUnit.DAYS) + 1;
                List<Leave> leavePendingByEID = leaveService.findPendingLeaveByEID(employeeId);
                int pendingLeaveAnnual = 0;
                int pendingLeaveMedical = 0;
                int pendingLeaveCompensation = 0;
                for (Leave l: leavePendingByEID){
                    if (l.getCategory().getName().equals("Annual")){
                        pendingLeaveAnnual += l.getNumOfDays();
                    }
                    if (l.getCategory().getName().equals("Medical")){
                        pendingLeaveMedical += l.getNumOfDays();
                    }
                    if (l.getCategory().getName().equals("Compensation")){
                        pendingLeaveCompensation += l.getNumOfDays();
                    }
                }

                if(leaveDays <= 14){
                    int workDays = 0;
                    for (int i = 0; i < leaveDays; i++){
                        LocalDate start = fromDate.plusDays(i);
                        if (start.getDayOfWeek() != DayOfWeek.SATURDAY && start.getDayOfWeek() != DayOfWeek.SUNDAY){
                            List<PublicHoliday> phAll = phService.findAllHolidays();
                            if (phAll.size() != 0){
                                for (PublicHoliday ph: phService.findAllHolidays()){
                                    if(start.isBefore(ph.getFromDate()) || start.isAfter(ph.getToDate()) && 
                                    !start.isEqual(ph.getFromDate()) && !start.isEqual(ph.getToDate())){
                                        workDays += 1;
                                    }
                                }
                            }
                            else{
                                workDays += 1;
                            }
                        }
                    }
                    if (leave.getCategory().getName().equals("Annual")){
                        int totalLeaveAnnual = 0;
                        for (Role r: roles){
                            if (r.getRoleId().equals("manager")){
                                totalLeaveAnnual = categoryService.findCategoryByName("Annual").getDaysOff();
                                break;
                            }
                            else{
                                totalLeaveAnnual = categoryService.findCategoryByName("Annual").getDaysOff() - categoryService.findCategoryByName("Annual").getDifferenceForAdmin();
                            }
                        }
                        int annualLeaveBal = 0;
                        annualLeaveBal = totalLeaveAnnual - user.getAnnualLeaveUsed() - pendingLeaveAnnual;
                        if (workDays > annualLeaveBal){
                            errors.rejectValue("toDate", "error.toDate", "Insufficient annual leave");
                        }
                    }
                    if (leave.getCategory().getName().equals("Medical")){
                        int medicalLeaveBal = categoryService.findCategoryByName("Medical").getDaysOff() - user.getMedicalLeaveUsed() - pendingLeaveMedical;
                        if (workDays > medicalLeaveBal){
                            errors.rejectValue("toDate", "error.toDate", "Insufficient medical leave");
                        }
                    }
                    if (leave.getCategory().getName().equals("Compensation")){
                        Double compensationLeave = overtimeCalculateService.findcompensationdaysByEid(employeeId);
                        double compensationLeaveBal = 0.0;
                        if (compensationLeave != null){
                            compensationLeaveBal = compensationLeave - user.getCompensationLeaveUsed() - pendingLeaveCompensation;
                        }
                        if (workDays > compensationLeaveBal){
                            errors.rejectValue("toDate", "error.toDate", "Insufficient compensation leave");
                        }
                    }
                    
                }
                else{
                    if (leave.getCategory().getName().equals("Annual")){
                        int totalLeaveAnnual = 0;
                        for (Role r: roles){
                            if (r.getRoleId().equals("manager")){
                                totalLeaveAnnual = categoryService.findCategoryByName("Annual").getDaysOff();
                                break;
                            }
                            else{
                                totalLeaveAnnual = categoryService.findCategoryByName("Annual").getDaysOff() - categoryService.findCategoryByName("Annual").getDifferenceForAdmin();
                            }
                        }
                        int annualLeaveBal = totalLeaveAnnual - user.getAnnualLeaveUsed() - pendingLeaveAnnual;
                        if (leaveDays > annualLeaveBal){
                            errors.rejectValue("toDate", "error.toDate", "Insufficient annual leave");
                        }
                    }
                    if (leave.getCategory().getName().equals("Medical")){
                        int medicalLeaveBal = categoryService.findCategoryByName("Medical").getDaysOff() - user.getMedicalLeaveUsed() - pendingLeaveMedical;
                        if (leaveDays > medicalLeaveBal){
                            errors.rejectValue("toDate", "error.toDate", "Insufficient medical leave");
                        }
                    }
                    if (leave.getCategory().getName().equals("Compensation")){
                        Double compensationLeave = overtimeCalculateService.findcompensationdaysByEid(employeeId);
                        double compensationLeaveBal = 0.0;
                        if (compensationLeave != null){
                            compensationLeaveBal = compensationLeave - user.getCompensationLeaveUsed() - pendingLeaveCompensation;
                        }
                        if (leaveDays > compensationLeaveBal){
                            errors.rejectValue("toDate", "error.toDate", "Insufficient compensation leave");
                        }
                    }
                }
            }
        }
    }
}