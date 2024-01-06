package javaCA.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

@Entity
public class Overtime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String employeeId;
    @NotNull(message = "The date is required")
    @Past(message = "The date must be in the past")
    private LocalDate overtimeDate;
    @NotNull(message = "The timeFrom field is required")
    private LocalTime comeTime;
    @NotNull(message = "The timeTo field is required")
    private LocalTime leaveTime;
    private double overtimeHours;

    @Column(name = "status", columnDefinition = "ENUM('APPLIED', 'UPDATED', 'DELETED', 'CANCELLED', 'APPROVED', 'REJECTED')")
    @Enumerated(EnumType.STRING)
    private LeaveStatusEnum status;
    
    // private OvertimeCalculate overtimeCalculate;

    public Overtime() {
    }

    public Overtime(String employeeId, LocalDate overtimeDate, LocalTime comeTime, LocalTime leaveTime,
            double overtimeHours,
            LeaveStatusEnum status) {
        this.employeeId = employeeId;
        this.overtimeDate = overtimeDate;
        this.comeTime = comeTime;
        this.leaveTime = leaveTime;
        this.overtimeHours = overtimeHours;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDate getOvertimeDate() {
        return overtimeDate;
    }

    public void setOvertimeDate(LocalDate overtimeDate) {
        this.overtimeDate = overtimeDate;
    }

    public LocalTime getComeTime() {
        return comeTime;
    }

    public void setComeTime(LocalTime comeTime) {
        this.comeTime = comeTime;
    }

    public LocalTime getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(LocalTime leaveTime) {
        this.leaveTime = leaveTime;
    }

    public double getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(double overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public LeaveStatusEnum getStatus() {
        return status;
    }

    public void setStatus(LeaveStatusEnum status) {
        this.status = status;
    }

}