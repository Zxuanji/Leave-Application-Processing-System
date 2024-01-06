package javaCA.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javaCA.model.OvertimeCalculate;
import javaCA.repository.OvertimeCalculateRepository;

@Service
@Transactional(readOnly = true)
public class OvertimeCalculateServiceImpl implements OvertimeCalculateService {
    @Autowired
    private OvertimeCalculateRepository overtimeCalculateRepository;

    @Override
    @Transactional(readOnly = false)
    public OvertimeCalculate createCalculate(OvertimeCalculate overtimeCalculate) {
        return overtimeCalculateRepository.save(overtimeCalculate);
    }

    @Override
    public Double findOvertimeHoursByEid(String id) {
        return overtimeCalculateRepository.findOvertimeHoursByEid(id);
    }

    @Override
    public Double findcompensationdaysByEid(String id) {
        return overtimeCalculateRepository.findcompensationdaysByEid(id);
    }

    @Override
    public OvertimeCalculate findByEmployeeId(String id) {
        return overtimeCalculateRepository.findByEmployeeId(id);
    }

    @Override
    public List<OvertimeCalculate> findapproved() {
        return overtimeCalculateRepository.findapproved();
    }

    @Override
    public Double findApprovedCompensationdays(String id) {
        return overtimeCalculateRepository.findApprovedCompensationdays(id);
    }

}