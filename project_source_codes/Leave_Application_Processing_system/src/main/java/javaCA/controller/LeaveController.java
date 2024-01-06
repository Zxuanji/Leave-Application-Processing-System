package javaCA.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import javaCA.model.Category;
import javaCA.model.Leave;
import javaCA.model.LeaveStatusEnum;
import javaCA.model.PublicHoliday;
import javaCA.model.Role;
import javaCA.model.User;
import javaCA.model.UserSession;
import javaCA.service.CategoryService;
import javaCA.service.LeaveService;
import javaCA.service.OvertimeCalculateService;
import javaCA.service.PublicHolidayService;
import javaCA.service.UserService;
import javaCA.validator.LeaveValidator;

@Controller
@RequestMapping("/leave")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private PublicHolidayService phService;
    @Autowired
    private LeaveValidator leaveValidator;
    @Autowired
    private OvertimeCalculateService overtimeCalculateService;

    @InitBinder("leave")
    private void initCourseBinder(WebDataBinder binder) {
        binder.addValidators(leaveValidator);
    }

    @GetMapping("/main")
    public String mainLeave(HttpSession session, Model model){
        UserSession userSession = (UserSession) session.getAttribute("user");
        User user = userSession.getUser();
        List<Role> roles = user.getRoleSet();
        String employeeId = user.getEmployeeId();
        model.addAttribute("user", user);
        model.addAttribute("categories", categoryService.findAllCategories());

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
        model.addAttribute("pendingLeaveAnnual", pendingLeaveAnnual);
        model.addAttribute("pendingLeaveMedical", pendingLeaveMedical);
        model.addAttribute("pendingLeaveCompensation", pendingLeaveCompensation);

        int totalLeaveAnnual = 0;
        int totalLeaveMedical = categoryService.findCategoryByName("Medical").getDaysOff();
        Double totalLeaveCompensation = overtimeCalculateService.findcompensationdaysByEid(employeeId);
        double compensationLeaveBal = 0.0;
        if (totalLeaveCompensation != null){
            compensationLeaveBal = totalLeaveCompensation - user.getCompensationLeaveUsed() - pendingLeaveCompensation;
        }
        else{
            totalLeaveCompensation = 0.0;
        }
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
        int medicalLeaveBal = totalLeaveMedical - user.getMedicalLeaveUsed() - pendingLeaveMedical;

        model.addAttribute("totalLeaveAnnual", totalLeaveAnnual);
        model.addAttribute("totalLeaveMedical", totalLeaveMedical);
        model.addAttribute("totalLeaveCompensation", totalLeaveCompensation);
        model.addAttribute("annualLeaveBal", annualLeaveBal);
        model.addAttribute("medicalLeaveBal", medicalLeaveBal);
        model.addAttribute("compensationLeaveBal", compensationLeaveBal);

        return "leave-main";
    }

    @GetMapping("/history")
    public String viewLeave(Model model, HttpSession session) {
        UserSession userSession = (UserSession) session.getAttribute("user");
        User user = userSession.getUser();
        String employeeId = user.getEmployeeId();
        model.addAttribute("leaves", leaveService.findLeaveByEID(employeeId));
        return "leave-history";
    }

    @GetMapping("/submission")
    public String createLeave(Model model, HttpSession session) {
        UserSession userSession = (UserSession) session.getAttribute("user");
        User user = userSession.getUser();
        List<Role> roles = user.getRoleSet();
        String employeeId = user.getEmployeeId();

        
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
        model.addAttribute("pendingLeaveAnnual", pendingLeaveAnnual);
        model.addAttribute("pendingLeaveMedical", pendingLeaveMedical);
        model.addAttribute("pendingLeaveCompensation", pendingLeaveCompensation);

        int totalLeaveAnnual = 0;
        int totalLeaveMedical = categoryService.findCategoryByName("Medical").getDaysOff();
        Double totalLeaveCompensation = overtimeCalculateService.findcompensationdaysByEid(employeeId);
        double compensationLeaveBal = 0.0;
        if (totalLeaveCompensation != null){
            compensationLeaveBal = totalLeaveCompensation - user.getCompensationLeaveUsed() - pendingLeaveCompensation;
        }
        else{
            totalLeaveCompensation = 0.0;
        }
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
        int medicalLeaveBal = totalLeaveMedical - user.getMedicalLeaveUsed() - pendingLeaveMedical;


        Leave leave = new Leave();
        leave.setEmployeeId(employeeId);
        leave.setStatus(LeaveStatusEnum.APPLIED);
        model.addAttribute("leave", leave);
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("annualLeaveBal", annualLeaveBal);
        model.addAttribute("medicalLeaveBal", medicalLeaveBal);
        model.addAttribute("compensationLeaveBal", compensationLeaveBal);
        session.setAttribute("annualLeaveBal1", annualLeaveBal);
        session.setAttribute("medicalLeaveBal1", medicalLeaveBal);
        session.setAttribute("compensationLeaveBal1", compensationLeaveBal);

        return "leave-submission";
    }

    @PostMapping("/submission")
    public String createLeave(@Valid @ModelAttribute("leave") Leave inLeave, BindingResult bindingResult, Model model, HttpSession session) {
        if (bindingResult.hasErrors()) {
            UserSession userSession = (UserSession) session.getAttribute("user");
            User user = userSession.getUser();
            model.addAttribute("user", user);
            model.addAttribute("categories", categoryService.findAllCategories());
            model.addAttribute("annualLeaveBal", session.getAttribute("annualLeaveBal1"));
            model.addAttribute("medicalLeaveBal", session.getAttribute("medicalLeaveBal1"));
            model.addAttribute("compensationLeaveBal", session.getAttribute("compensationLeaveBal1"));

            return "leave-submission";
        }

        LocalDate fromDate = inLeave.getFromDate();
        LocalDate toDate = inLeave.getToDate();
        int leaveDays = (int) fromDate.until(toDate, ChronoUnit.DAYS) + 1;
        int workDays = 0;
        for (int i = 0; i < leaveDays; i++){
            LocalDate start = fromDate.plusDays(i);
            System.out.println(start);
            if (start.getDayOfWeek() != DayOfWeek.SATURDAY && start.getDayOfWeek() != DayOfWeek.SUNDAY){
                List<PublicHoliday> phAll = phService.findAllHolidays();
                if (phAll.size() != 0){
                    for (PublicHoliday ph: phAll){
                        if(start.isBefore(ph.getFromDate()) || start.isAfter(ph.getToDate()) && 
                        !start.isEqual(ph.getFromDate()) && !start.isEqual(ph.getToDate())){
                            workDays += 1;
                            System.out.println(workDays);
                        }
                    }
                }
                else{
                    workDays += 1;
                }
            }
        }
        if(leaveDays <= 14){
            inLeave.setNumOfDays(workDays);
        }
        else{
            inLeave.setNumOfDays(leaveDays);
        }
        inLeave.setContactDetails("Not Applicable");
        leaveService.createLeave(inLeave);
        return "redirect:/leave/history";
    }

    @GetMapping("/application/{leaveId}")
    public String detailedLeave(@PathVariable("leaveId") int leaveId, Model model) {
        Optional<Leave> optLeave = leaveService.findLeave(leaveId);
        Leave leave = optLeave.get();
        model.addAttribute("leave", leave);
        model.addAttribute("leaveId", leaveId);
        model.addAttribute("isRejected", leave.getStatus().equals(LeaveStatusEnum.REJECTED));
        model.addAttribute("isApproved", leave.getStatus().equals(LeaveStatusEnum.APPROVED));
        model.addAttribute("isApplied", leave.getStatus().equals(LeaveStatusEnum.APPLIED));
        model.addAttribute("isUpdated", leave.getStatus().equals(LeaveStatusEnum.UPDATED));
        return "leave-application";
    }

    @PostMapping("/application/{leaveId}")
    public String detailedLeave(@PathVariable("leaveId") int leaveId, Leave leave){
        return "redirect:/leave/application/update/" + leaveId;
    }

    @GetMapping("/application/update/{leaveId}")
    public String updateLeave(@PathVariable("leaveId") int leaveId, Model model, HttpSession session) {
        Optional<Leave> optLeave = leaveService.findLeave(leaveId);
        Leave leave = optLeave.get();
        leave.setStatus(LeaveStatusEnum.UPDATED);
        UserSession userSession = (UserSession) session.getAttribute("user");
        User user = userSession.getUser();
        List<Role> roles = user.getRoleSet();
        String employeeId = user.getEmployeeId();
        
        model.addAttribute("leave", leave);
        model.addAttribute("user", user);
        model.addAttribute("categories", categoryService.findAllCategories());

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

        int totalLeaveAnnual = 0;
        int totalLeaveMedical = categoryService.findCategoryByName("Medical").getDaysOff();
        Double totalLeaveCompensation = overtimeCalculateService.findcompensationdaysByEid(employeeId);
        double compensationLeaveBal = 0.0;
        if (totalLeaveCompensation != null){
            compensationLeaveBal = totalLeaveCompensation - user.getCompensationLeaveUsed() - pendingLeaveCompensation;
        }
        else{
            totalLeaveCompensation = 0.0;
        }
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
        int medicalLeaveBal = totalLeaveMedical - user.getMedicalLeaveUsed() - pendingLeaveMedical;

        model.addAttribute("annualLeaveBal", annualLeaveBal);
        model.addAttribute("medicalLeaveBal", medicalLeaveBal);
        model.addAttribute("compensationLeaveBal", compensationLeaveBal);
        session.setAttribute("annualLeaveBal2", annualLeaveBal);
        session.setAttribute("medicalLeaveBal2", medicalLeaveBal);
        session.setAttribute("compensationLeaveBal2", compensationLeaveBal);
        
        return "leave-update";
    }

    @PostMapping("/application/update/{leaveId}")
    public String updateLeave(@PathVariable("leaveId") int leaveId, @Valid @ModelAttribute("leave") Leave inLeave,
            BindingResult bindingResult, Model model, HttpSession session) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAllCategories());
            model.addAttribute("annualLeaveBal", session.getAttribute("annualLeaveBal2"));
            model.addAttribute("medicalLeaveBal", session.getAttribute("medicalLeaveBal2"));
            model.addAttribute("compensationLeaveBal", session.getAttribute("compensationLeaveBal2"));
            return "leave-update";
        }
        
        LocalDate fromDate = inLeave.getFromDate();
        LocalDate toDate = inLeave.getToDate();
        int leaveDays = (int) fromDate.until(toDate, ChronoUnit.DAYS) + 1;
        int workDays = 0;
        for (int i = 0; i < leaveDays; i++){
            LocalDate start = fromDate.plusDays(i);
            System.out.println(start);
            if (start.getDayOfWeek() != DayOfWeek.SATURDAY && start.getDayOfWeek() != DayOfWeek.SUNDAY){
                List<PublicHoliday> phAll = phService.findAllHolidays();
                if (phAll.size() != 0){
                    for (PublicHoliday ph: phAll){
                        if(start.isBefore(ph.getFromDate()) || start.isAfter(ph.getToDate()) && 
                        !start.isEqual(ph.getFromDate()) && !start.isEqual(ph.getToDate())){
                            workDays += 1;
                            System.out.println(workDays);
                        }
                    }
                }
                else{
                    workDays += 1;
                }
            }
        }
        if(leaveDays <= 14){
            inLeave.setNumOfDays(workDays);
        }
        else{
            inLeave.setNumOfDays(leaveDays);
        }

        leaveService.saveLeave(inLeave);
        return "redirect:/leave/history";
    }

    @GetMapping("/application/cancel/{leaveId}")
    public String cancelLeave(@PathVariable("leaveId") int leaveId, HttpSession session) {

        Optional<Leave> optLeave = leaveService.findLeave(leaveId);
        Leave leave = optLeave.get();
        leave.setStatus(LeaveStatusEnum.CANCELLED);

        UserSession userSession = (UserSession) session.getAttribute("user");
        User user = userSession.getUser();
        int leaveApplied = leave.getNumOfDays();
        if (leave.getCategory().getName().equals("Annual")) {
            int leaveAnnual = user.getAnnualLeaveUsed();
            user.setAnnualLeaveUsed(leaveAnnual - leaveApplied);
        }
        else if(leave.getCategory().getName().equals("Medical")) {
            int leaveMedical = user.getMedicalLeaveUsed();
            user.setMedicalLeaveUsed(leaveMedical - leaveApplied);
        }
        else {
            int leaveCompensation = user.getCompensationLeaveUsed();
            user.setCompensationLeaveUsed(leaveCompensation - leaveApplied);
        }

        leave.setNumOfDays(0);
        leaveService.saveLeave(leave);
        userService.changeUser(user);
        return "redirect:/leave/history";
    }

    @GetMapping("/application/delete/{leaveId}")
    public String deleteLeave(@PathVariable("leaveId") int leaveId){
        Optional<Leave> optLeave = leaveService.findLeave(leaveId);
        Leave leave = optLeave.get();
        leave.setStatus(LeaveStatusEnum.DELETED);
        leave.setNumOfDays(0);
        leaveService.saveLeave(leave);
        return "redirect:/leave/history";
    }

}
