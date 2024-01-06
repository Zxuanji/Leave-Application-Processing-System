package javaCA.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import javaCA.model.Overtime;
import javaCA.repository.OvertimeRepository;

@Service
@Transactional(readOnly = true)
public class OvertimeServiceImpl implements OvertimeService {
    @Resource
    private OvertimeRepository overtimeRepository;

    @Override
    public List<Overtime> findAllOvertime() {
        return overtimeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = false)
    public Overtime createOvertime(Overtime overtime) {
        return overtimeRepository.saveAndFlush(overtime);
    }

    @Override
    @Transactional(readOnly = false)
    public Overtime changeOvertime(Overtime overtime) {
        return overtimeRepository.saveAndFlush(overtime);
    }

    @Override
    public List<Overtime> findOvertimeByEID(String employeeId) {
        return overtimeRepository.findOvertimeByEID(employeeId);
    }

    @Override
    public List<Overtime> findPendingOvertimeByEID(String employeeId) {
        return overtimeRepository.findPendingOvertimeByEID(employeeId);
    }

    @Override
    @Transactional(readOnly = false)
    public Overtime findOvertimeById(int id) {
        Optional<Overtime> optionalOvertime = overtimeRepository.findById(id);
        return optionalOvertime.orElse(null);
    }

    @Override
    public List<Overtime> findOvertimeByStatus() {
        return overtimeRepository.findOvertimeByStatus();
    }
}