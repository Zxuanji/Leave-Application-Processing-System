package javaCA.service;


import java.util.List;

import javaCA.model.OvertimeCalculate;

public interface OvertimeCalculateService {
    OvertimeCalculate createCalculate(OvertimeCalculate overtimeCalculate);

    Double findOvertimeHoursByEid(String id);

    Double findcompensationdaysByEid(String id);

    Double findApprovedCompensationdays(String id);

    OvertimeCalculate findByEmployeeId(String id);


    List<OvertimeCalculate> findapproved();
}