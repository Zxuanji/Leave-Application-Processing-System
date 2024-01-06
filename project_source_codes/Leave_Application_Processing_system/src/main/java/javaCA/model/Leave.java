package javaCA.model;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "leavetable")
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int leaveId;

    private String employeeId;

    @NotNull(message = "Leave start date is required")
    private LocalDate fromDate;

    @NotNull(message = "Leave end date is required")
    private LocalDate toDate;

    private int numOfDays;

    @NotBlank(message = "Reason for leave is required")
    private String additionalReasons;

    private String workDissemination;
    private String contactDetails;

    private String comment;

    @Column(name = "status", columnDefinition = "ENUM('APPLIED', 'UPDATED', 'DELETED', 'CANCELLED', 'APPROVED', 'REJECTED')")
    @Enumerated(EnumType.STRING)
    private LeaveStatusEnum status;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    public Leave() {}
    public Leave(String employeeId, LocalDate fromDate, LocalDate toDate,
            String additionalReasons, String workDissemination, String contactDetails, Category category,
            LeaveStatusEnum status) {
        this.employeeId = employeeId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.numOfDays = 0;
        this.additionalReasons = additionalReasons;
        this.workDissemination = workDissemination;
        this.contactDetails = contactDetails;
        this.category = category;
        this.status = status;
        this.comment = "";
    }

    public int getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(int leaveId) {
        this.leaveId = leaveId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public int getNumOfDays(){
        return numOfDays;
    }

    public void setNumOfDays(int numOfDays){
        this.numOfDays = numOfDays;
    }

    public String getAdditionalReasons() {
        return additionalReasons;
    }

    public void setAdditionalReasons(String additionalReasons) {
        this.additionalReasons = additionalReasons;
    }

    public String getWorkDissemination() {
        return workDissemination;
    }

    public void setWorkDissemination(String workDissemination) {
        this.workDissemination = workDissemination;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    public LeaveStatusEnum getStatus() {
        return status;
    }

    public void setStatus(LeaveStatusEnum status) {
        this.status = status;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Leave ID: " + leaveId +
                "\nEmployee ID: " + employeeId +
                "\nFrom: " + fromDate +
                "\nTo: " + toDate +
                "\nAdditional Reason: " + additionalReasons +
                "\nWork Dissemination (if any): " + workDissemination +
                "\nContact (if on an overseas trip): " + contactDetails +
                "\nApplication Status: " + status +
                "\n" + category;
    }
}
