package javaCA.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import javaCA.model.Role;
import javaCA.repository.RoleRepository;

@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {
	@Resource
	private RoleRepository roleRepository;

	@Override
	public List<Role> findAllRoles() {
		return roleRepository.findAll();
	}
	
	@Override
	public Role findRoleById(String id) {
		return roleRepository.findById(id).orElse(null);
	}
	
	@Override
	@Transactional(readOnly = false)
	public Role createRole(Role role) {
		return roleRepository.saveAndFlush(role);
	}
	
	@Override
	@Transactional(readOnly = false)
	public Role changeRole(Role role) {
		return roleRepository.saveAndFlush(role);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void removeRole(Role role) {
		roleRepository.delete(role);
	}
}
