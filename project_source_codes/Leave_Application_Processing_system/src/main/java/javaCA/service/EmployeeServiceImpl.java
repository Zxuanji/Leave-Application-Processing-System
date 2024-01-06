package javaCA.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import javaCA.model.Employee;
import javaCA.repository.EmployeeRepository;

@Service
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {
	@Resource
	private EmployeeRepository employeeRepository;
	
    @Override
    public List<Employee> findEmployeesByManager(String s) {
        return employeeRepository.findEmployeesByManagerId(s);
    }
    
    @Override
    public Employee findEmployee(String empid) {
        return employeeRepository.findById(empid).orElse(null);

    }
    
    @Override
    public List<Employee> findAllManagers() {
        return employeeRepository.findAllManagers();
    }
	
	@Override
	public Employee findEmployeeById(String id) {
//		return employeeRepository.findEmployeeById(id);
		return employeeRepository.findById(id).orElse(null);
	}
	
	@Override
	public List<Employee> findSubordinates(String id){
		return employeeRepository.findSubordinates(id);
	}
	
	@Override
	public List<Employee> findAllEmployees(){
		return employeeRepository.findAll();
	}
	
	@Override
	public List<String> findAllEmployeeIDs(){
		return employeeRepository.findAllEmployeeIDs();
	}
	
	@Override
	@Transactional(readOnly = false)
	public Employee createEmployee(Employee emp) {
		return employeeRepository.saveAndFlush(emp);
	}
	
	@Override
	@Transactional(readOnly = false)
	public Employee changeEmployee(Employee emp) {
		return employeeRepository.saveAndFlush(emp);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void removeEmployee(Employee emp) {
		employeeRepository.delete(emp);
	}
}
