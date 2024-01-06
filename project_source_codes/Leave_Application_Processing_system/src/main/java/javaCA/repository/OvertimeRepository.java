package javaCA.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javaCA.model.Overtime;

public interface OvertimeRepository extends JpaRepository<Overtime, Integer> {
    @Query(value = "select o from Overtime o where o.employeeId = :eid")
    public List<Overtime> findOvertimeByEID(@Param("eid") String id);

    @Query(value = "select o from Overtime o where o.employeeId = :eid and o.status= 'APPLIED' ")
    public List<Overtime> findPendingOvertimeByEID(@Param("eid") String id);

    @Query("SELECT o FROM Overtime o WHERE o.status = 'APPROVED' ")
    public List<Overtime> findOvertimeByStatus();
}