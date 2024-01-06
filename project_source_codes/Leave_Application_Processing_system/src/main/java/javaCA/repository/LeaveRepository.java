package javaCA.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javaCA.model.Leave;

public interface LeaveRepository extends JpaRepository<Leave, Integer> {
	@Query(value = "select l from Leave l where l.employeeId = :eid")
	public List<Leave> findLeaveByEID(@Param("eid") String id);

	@Query(value = "select l from Leave l where l.employeeId = :eid and (l.status = 'APPLIED' or l.status = 'UPDATED' )")
	public List<Leave> findPendingLeaveByEID(@Param("eid") String eid);

	@Query(value = "select l from Leave l where l.employeeId = :eid and (l.status = 'APPROVED' or l.status = 'REJECTED')")
	public List<Leave> findDecidedLeaveByEID(@Param("eid") String eid);
}
