package javaCA.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javaCA.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

	@Query("SELECT e FROM Employee e WHERE e.employeeId = :id")
	Employee findEmployeeById(@Param("id") String id);

	@Query("SELECT DISTINCT e2 FROM Employee e1, Employee e2 WHERE e1.employeeId = :id AND e1.employeeId = e2.managerId")
	List<Employee> findSubordinates(@Param("id") String id);

	@Query("SELECT DISTINCT e.employeeId FROM Employee e")
	List<String> findAllEmployeeIDs();

	@Query(value = "select e from Employee e where e.managerId = :mgrid")
	List<Employee> findEmployeesByManagerId(@Param("mgrid") String mgrid);

	@Query(value = "SELECT DISTINCT m FROM Employee e, Employee m where e.managerId = m.employeeId ")
	List<Employee> findAllManagers();
}
