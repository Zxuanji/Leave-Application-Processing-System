package javaCA.service;

import java.util.List;

import javaCA.model.Role;

public interface RoleService {
	List<Role> findAllRoles();
	Role createRole(Role role);
	Role findRoleById(String id);
	Role changeRole(Role role);
	void removeRole(Role role);
}
