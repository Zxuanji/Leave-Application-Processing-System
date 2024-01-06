package javaCA.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import javaCA.model.Approve;
import javaCA.model.Employee;
import javaCA.model.Leave;
import javaCA.model.LeaveStatusEnum;
import javaCA.model.Overtime;
import javaCA.model.User;
import javaCA.model.UserSession;
import javaCA.service.LeaveService;
import javaCA.service.OvertimeService;
import javaCA.service.UserService;

import javaCA.validator.ApproveValidator;

@Controller
@RequestMapping("manager")
public class ManagerController {
    @Autowired
    private LeaveService leaveService;

    @Autowired
    private UserService userService;

    @Autowired
    private OvertimeService overtimeService;

    @Autowired
    private ApproveValidator approveValidator;

    @InitBinder("approve")
    private void initApproveBinder(WebDataBinder binder) {
        binder.addValidators(approveValidator);
    }

    @RequestMapping(value = "/pending") // show all the leaves without confirming status
    public String pendingApprovals(Model model, HttpSession session) {

        UserSession userSession = (UserSession) session.getAttribute("user");// 参数可能要改

        List<Employee> employees = userSession.getSubordinates();
        Map<Employee, List<Leave>> map = new HashMap<>();

        for (Employee employee : employees) {
            map.put(employee, leaveService.findPendingLeaveByEID(employee.getEmployeeId()));
        }

        model.addAttribute("pendingLeave", map);

        return "manager-leave-pending";
    }

    @RequestMapping(value = "/subordinates-history") // show the subordinates' history
    public String subordinatesHistory(HttpSession session, Model model) {
        UserSession userSession = (UserSession) session.getAttribute("user");//
        List<Employee> employees = userSession.getSubordinates();

        Map<Employee, List<Leave>> map = new HashMap<>();

        for (Employee employee : employees) {
            map.put(employee, leaveService.findDecidedLeaveByEID(employee.getEmployeeId()));
        }

        model.addAttribute("DecidedLeave", map);
        return "manager-subordinate-leave-history";
    }

    @GetMapping("/leave/display/{id}")
    public String updateDetailsPage(Model model, @PathVariable("id") Integer lid) {
        String eid = leaveService.findLeave(lid).getEmployeeId();
        model.addAttribute("DecidedLeave", leaveService.findDecidedLeaveByEID(eid));
        model.addAttribute("approve", new Approve());
        model.addAttribute("leave", leaveService.findLeave(lid));
        Leave leave = leaveService.findLeave(lid);
        User user = userService.findUserByEmployeeId(leave.getEmployeeId());
        model.addAttribute("annualLeaveUsed", user.getAnnualLeaveUsed());
        model.addAttribute("medicalLeaveUsed", user.getMedicalLeaveUsed());
        return "manager-leave-detail";
    }

    @PostMapping("/leave/display/{id}")
    public String approveOrRejectLeave(@Valid @ModelAttribute("approve") Approve approve, BindingResult bindingResult,
            @PathVariable("id") Integer id, Model model, HttpSession session) {//

        if (bindingResult.hasErrors()) {
            model.addAttribute("leave", leaveService.findLeave(id));
            return "manager-leave-detail";
        }

        Leave leave = leaveService.findLeave(id);

        if (approve.getDecision().trim().equalsIgnoreCase(LeaveStatusEnum.APPROVED.toString())) {
            leave.setStatus(LeaveStatusEnum.APPROVED);

            User user = userService.findUserByEmployeeId(leave.getEmployeeId());
            if (leave.getCategory().getName().equals("Annual")) {
                user.setAnnualLeaveUsed(user.getAnnualLeaveUsed() + leave.getNumOfDays());
                userService.changeUser(user);
            } else if (leave.getCategory().getName().equals("Medical")) {
                user.setMedicalLeaveUsed(user.getMedicalLeaveUsed() + leave.getNumOfDays());
                userService.changeUser(user);
            }

        } else if (approve.getDecision().trim().equalsIgnoreCase(LeaveStatusEnum.REJECTED.toString())) {
            leave.setStatus(LeaveStatusEnum.REJECTED);
            leave.setNumOfDays(0);
        }

        leave.setComment(approve.getComment());

        leaveService.changeLeave(leave);

        return "redirect:/manager/pending";
    }

    @GetMapping(value = "/overtime/certification")
    public String overtimeCertification(Model model, HttpSession session) {
        UserSession userSession = (UserSession) session.getAttribute("user");// 参数可能要改

        List<Employee> employees = userSession.getSubordinates();
        Map<Employee, List<Overtime>> map = new HashMap<>();

        for (Employee employee : employees) {
            map.put(employee, overtimeService.findPendingOvertimeByEID(employee.getEmployeeId()));
        }

        model.addAttribute("pendingOvertime", map);

        return "manager-overtime-pending";
    }

    @PostMapping(value = "/overtime/certification/all")
    public String approveOvertime(HttpSession session) {
        UserSession userSession = (UserSession) session.getAttribute("user");//

        List<Employee> employees = userSession.getSubordinates();
        Map<Employee, List<Overtime>> map = new HashMap<>();

        for (Employee employee : employees) {
            map.put(employee,
                    overtimeService.findPendingOvertimeByEID(employee.getEmployeeId()));
        }

        for (Employee employee : map.keySet()) {
            for (Overtime overtime : map.get(employee)) {
                overtime.setStatus(LeaveStatusEnum.APPROVED);
                overtimeService.changeOvertime(overtime);
            }
        }
        return "redirect:/manager/overtime/certification";
    }

    @GetMapping(value = "/overtime/approve/{id}")
    public String approveSingleOvertime(@PathVariable("id") int id) {
        Overtime overtime = overtimeService.findOvertimeById(id);
        if (overtime != null) {
            overtime.setStatus(LeaveStatusEnum.APPROVED);
            overtimeService.changeOvertime(overtime);
        }
        return "redirect:/manager/overtime/certification";
    }

    @GetMapping(value = "/overtime/reject/{id}")
    public String rejectSingleOvertime(@PathVariable("id") int id) {
        Overtime overtime = overtimeService.findOvertimeById(id);
        if (overtime != null) {
            overtime.setStatus(LeaveStatusEnum.REJECTED);
            overtimeService.changeOvertime(overtime);
        }
        return "redirect:/manager/overtime/certification";
    }
}
