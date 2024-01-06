package javaCA.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "myuser") // name cannot be user
public class User {

	@Id
	@Column(name = "userid")
	private String userId;

	@NotBlank(message = "{error.user.name.empty}")
	private String name;

	@NotBlank(message = "{error.user.password.empty}")
	private String password;

	@Column(name = "employeeid")
	private String employeeId;

	@Column(name = "annualleaveused")
	private int annualLeaveUsed;

	@Column(name = "medicalleaveused")
	private int medicalLeaveUsed;

	@Column(name = "compensationleaveused")
	private int compensationLeaveUsed;

	public int getAnnualLeaveUsed() {
		return annualLeaveUsed;
	}

	public void setAnnualLeaveUsed(int annualLeaveUsed) {
		this.annualLeaveUsed = annualLeaveUsed;
	}

	public int getMedicalLeaveUsed() {
		return medicalLeaveUsed;
	}

	public void setMedicalLeaveUsed(int medicalLeaveUsed) {
		this.medicalLeaveUsed = medicalLeaveUsed;
	}

	public int getCompensationLeaveUsed() {
		return compensationLeaveUsed;
	}

	public void setCompensationLeaveUsed(int compensationLeaveUsed) {
		this.compensationLeaveUsed = compensationLeaveUsed;
	}

	@ManyToMany(targetEntity = Role.class, cascade = { CascadeType.ALL, CascadeType.PERSIST }, fetch = FetchType.EAGER)
	@JoinTable(name = "userrole", joinColumns = {
			@JoinColumn(name = "userid", referencedColumnName = "userid") }, inverseJoinColumns = {
					@JoinColumn(name = "roleid", referencedColumnName = "roleid") })
	private List<Role> roleSet;

	// private LeaveEntitlement LeaveEntitlement;

	public User() {
	}

	public User(String userId, String name, String password, String employeeId) {// 创建user的时候Leave使用天数设置为0
		this.userId = userId;
		this.name = name;
		this.password = password;
		this.employeeId = employeeId;
		this.annualLeaveUsed = 0;
		this.medicalLeaveUsed = 0;
		this.compensationLeaveUsed = 0;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public List<Role> getRoleSet() {
		return roleSet;
	}

	public void setRoleSet(List<Role> roleSet) {
		this.roleSet = roleSet;
	}

	// public LeaveEntitlement getLeaveEntitlement() {
	// return LeaveEntitlement;
	// }
	//
	// public void setLeaveEntitlement(LeaveEntitlement leaveEntitlement) {
	// LeaveEntitlement = leaveEntitlement;
	// }

	public List<String> getRoleIds() {
		List<String> roleIds = new ArrayList<>();
		roleSet.forEach(r -> roleIds.add(r.getRoleId()));

		return roleIds;
	}
}
