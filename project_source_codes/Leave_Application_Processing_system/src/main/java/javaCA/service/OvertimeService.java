package javaCA.service;

import java.util.List;
import java.util.Optional;

import javaCA.model.Overtime;

public interface OvertimeService {
    public List<Overtime> findAllOvertime();

    public Overtime createOvertime(Overtime overtime);

    public Overtime changeOvertime(Overtime overtime);

    public List<Overtime> findOvertimeByEID(String employeeId);

    public List<Overtime> findPendingOvertimeByEID(String employeeId);

    public List<Overtime> findOvertimeByStatus();

    public Overtime findOvertimeById(int id);
}