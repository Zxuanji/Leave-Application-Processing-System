package javaCA.model;

import jakarta.persistence.*;

@Entity
public class OvertimeCalculate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String employeeId;
    private double overtimeHours;
    private double compensationdays;
    private double approvedCompensationdays;
    // private Overtime overtime;

    public OvertimeCalculate() {
    }

    public OvertimeCalculate(String employeeId, double overtimeHours, double compensationdays,
            double approvedCompensationdays) {
        this.employeeId = employeeId;
        this.overtimeHours = overtimeHours;
        this.compensationdays = compensationdays;
        this.approvedCompensationdays = approvedCompensationdays;
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

    public double getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(double overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public double getCompensationdays() {
        return compensationdays;
    }

    public void setCompensationdays(double compensationdays) {
        this.compensationdays = compensationdays;
    }

    public double getApprovedCompensationdays() {
        return approvedCompensationdays;
    }

    public void setApprovedCompensationdays(double approvedCompensationdays) {
        this.approvedCompensationdays = approvedCompensationdays;
    }

    @Override
    public String toString() {
        return "OvertimeCalculate [id=" + id + ", employeeId=" + employeeId + ", overtimeHours=" + overtimeHours
                + ", compensationdays=" + compensationdays + "]";
    }

}