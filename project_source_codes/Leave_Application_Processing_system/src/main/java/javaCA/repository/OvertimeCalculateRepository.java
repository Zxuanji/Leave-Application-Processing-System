package javaCA.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javaCA.model.OvertimeCalculate;

public interface OvertimeCalculateRepository extends JpaRepository<OvertimeCalculate, Integer> {
    @Query("SELECT o.overtimeHours FROM OvertimeCalculate o WHERE o.employeeId = :eid")
    Double findOvertimeHoursByEid(@Param("eid") String id);

    @Query("SELECT o.compensationdays FROM OvertimeCalculate o WHERE o.employeeId = :eid")
    Double findcompensationdaysByEid(@Param("eid") String id);

    @Query("SELECT o.approvedCompensationdays FROM OvertimeCalculate o WHERE o.employeeId = :eid")
    Double findApprovedCompensationdays(@Param("eid") String id);

    @Query("SELECT o FROM OvertimeCalculate o WHERE o.employeeId = :eid")
    OvertimeCalculate findByEmployeeId(@Param("eid") String id);



    @Query("SELECT DISTINCT oc FROM OvertimeCalculate oc, Overtime o WHERE o.employeeId = oc.employeeId AND o.status = 'APPROVED' ")
    List<OvertimeCalculate> findapproved();

}