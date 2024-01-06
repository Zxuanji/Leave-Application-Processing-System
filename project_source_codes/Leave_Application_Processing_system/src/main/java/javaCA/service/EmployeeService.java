package javaCA.service;

import java.util.List;

import javaCA.model.Employee;

public interface EmployeeService {
	Employee findEmployeeById(String id);
	List<Employee> findSubordinates(String id);
	List<Employee> findAllEmployees();
	List<String> findAllEmployeeIDs();
	Employee createEmployee(Employee emp);
	Employee changeEmployee(Employee emp);
	void removeEmployee(Employee emp);
	List<Employee> findEmployeesByManager(String s);
	Employee findEmployee(String empid);
	List<Employee> findAllManagers();
}
