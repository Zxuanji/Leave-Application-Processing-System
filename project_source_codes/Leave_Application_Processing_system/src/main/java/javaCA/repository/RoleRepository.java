package javaCA.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import javaCA.model.Role;

public interface RoleRepository extends JpaRepository<Role, String> {
	
}
