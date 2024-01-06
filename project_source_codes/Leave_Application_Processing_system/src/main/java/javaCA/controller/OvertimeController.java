package javaCA.controller;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import javaCA.model.LeaveStatusEnum;
import javaCA.model.Overtime;
import javaCA.model.OvertimeCalculate;
import javaCA.model.User;
import javaCA.model.UserSession;
import javaCA.service.OvertimeCalculateService;
import javaCA.service.OvertimeService;
import javaCA.validator.OvertimeValidator;

@Controller
@RequestMapping("/overtime")
public class OvertimeController {
    @Autowired
    private OvertimeService overtimeService;

    @Autowired
    private OvertimeCalculateService overtimeCalculateService;

    @Autowired
    private OvertimeValidator overtimeValidator;

    @InitBinder
    private void initOvertimeBinder(WebDataBinder binder) {
        binder.addValidators(overtimeValidator);
    }

    @GetMapping("/add")
    private String addOvertime(Model model, HttpSession session) {
        UserSession userSession = (UserSession) session.getAttribute("user");

        List<Overtime> st = overtimeService.findOvertimeByStatus();
        double approvedOvertime = 0;
        double approvedTotalCompensationDays = 0;
        for(Overtime status : st){
            approvedOvertime += calculateDurationTime(status.getComeTime(), status.getLeaveTime());
        }
        approvedTotalCompensationDays = ((double)((int)(approvedOvertime / 4))) / 2;


        model.addAttribute("overtime", new Overtime());
        model.addAttribute("applylist", overtimeService.findOvertimeByEID(userSession.getUser().getEmployeeId()));
        model.addAttribute("totalOvertimeHours",
                overtimeCalculateService.findOvertimeHoursByEid(userSession.getUser().getEmployeeId()));
        model.addAttribute("totalCompensationdays",
                overtimeCalculateService.findcompensationdaysByEid(userSession.getUser().getEmployeeId()));
        model.addAttribute("approvedCompensationdays", approvedTotalCompensationDays);
        return "overtime-submission";
    }

    @PostMapping("/add")
    private String addOvertime(@Valid @ModelAttribute("overtime") Overtime overtime, BindingResult bindingResult,
            Model model, HttpSession session) {
        if (bindingResult.hasErrors()) {
            UserSession userSession = (UserSession) session.getAttribute("user");
            // model.addAttribute("overtime", new Overtime());
            model.addAttribute("applylist", overtimeService.findOvertimeByEID(userSession.getUser().getEmployeeId()));
            model.addAttribute("totalOvertimeHours",
                    overtimeCalculateService.findOvertimeHoursByEid(userSession.getUser().getEmployeeId()));
            model.addAttribute("totalCompensationdays",
                    overtimeCalculateService.findcompensationdaysByEid(userSession.getUser().getEmployeeId()));
            return "overtime-submission";
        }
        // 知道user是谁
        UserSession userSession = (UserSession) session.getAttribute("user");
        overtime.setStatus(LeaveStatusEnum.APPLIED);
        overtime.setEmployeeId(userSession.getUser().getEmployeeId());
        overtime.setOvertimeHours(calculateDurationTime(overtime.getComeTime(), overtime.getLeaveTime()));

        overtimeService.createOvertime(overtime);

        OvertimeCalculate OC = overtimeCalculateService.findByEmployeeId(userSession.getUser().getEmployeeId());
        if (OC == null) {
            OC = new OvertimeCalculate();
        }
        // double approved = 0;
        // for(OvertimeCalculate x : overtimeCalculateService.findapproved()){
        //     System.out.println(x.getCompensationdays());
        //     approved = x.getCompensationdays();
        //     System.out.println();
        // }
        // OC.setApprovedCompensationdays(approved);

        OC.setEmployeeId(userSession.getUser().getEmployeeId());
        Double totalOvertimeHours = overtimeCalculateService
                .findOvertimeHoursByEid(userSession.getUser().getEmployeeId());
        if (totalOvertimeHours == null) {
            totalOvertimeHours = 0.0;
        }
        totalOvertimeHours += calculateDurationTime(overtime.getComeTime(), overtime.getLeaveTime());

        Double totalCompensationdays = overtimeCalculateService
                .findcompensationdaysByEid(userSession.getUser().getEmployeeId());

        if (totalCompensationdays == null) {
            totalCompensationdays = 0.0;
        }
        totalCompensationdays = ((double) ((int) (totalOvertimeHours / 4))) / 2;
    
        OC.setOvertimeHours(totalOvertimeHours);
        OC.setCompensationdays(totalCompensationdays);
        overtimeCalculateService.createCalculate(OC);

        List<Overtime> st = overtimeService.findOvertimeByStatus();
        double approvedOvertime = 0;
        double approvedTotalCompensationDays = 0;
        for(Overtime status : st){
            approvedOvertime += calculateDurationTime(status.getComeTime(), status.getLeaveTime());
        }
        approvedTotalCompensationDays = ((double)((int)(approvedOvertime / 4))) / 2;
        model.addAttribute("approvedCompensationdays", approvedTotalCompensationDays);

        model.addAttribute("totalOvertimeHours", totalOvertimeHours);

        model.addAttribute("totalCompensationdays", totalCompensationdays);

        List<Overtime> appList = overtimeService.findOvertimeByEID(userSession.getUser().getEmployeeId());

        model.addAttribute("applylist", appList);
        return "overtime-submission";
    }

    // 将加班时间换算成小时，满1个小时计1小时，满半小时不满1小时计0.5小时
    public static double calculateDurationTime(LocalTime start, LocalTime end) {
        Duration duration = Duration.between(start, end);
        double totalMinutes = duration.toMinutes();

        if (totalMinutes < 30) {
            return 0;
        } else {
            double hours = (int) totalMinutes / 60; // 完整小时数
            double remainingMinutes = totalMinutes % 60;// 剩余分钟数
            if (remainingMinutes > 30) {
                hours += 0.5;
            }
            return hours;
        }
    }

    // 计算总共加班时间和可获得compensation的天数
    public static Map<String, Double> calculateall(List<Overtime> appList) {
        double totalOvertimeHours = 0;
        double totalCompensationhalfdays = 0;
        double totalCompensationdays = 0;
        for (Overtime row : appList) {
            totalOvertimeHours += row.getOvertimeHours();
        }
        totalCompensationhalfdays = (int) totalOvertimeHours / 4;
        totalCompensationdays = totalCompensationhalfdays / 2;
        Map<String, Double> results = new HashMap<>();
        results.put("totalOvertimeHours", totalOvertimeHours);
        results.put("totalCompensationdays", totalCompensationdays);
        return results;
    }

}