package javaCA.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javaCA.model.User;

public interface UserRepository extends JpaRepository<User, String> {
	
	@Query("SELECT u FROM User u WHERE u.name = :username AND u.password = :pwd") // User: model class name, not table name
	User findUserByNamePwd(@Param("username") String username, @Param("pwd") String pwd);

	@Query("Select u FROM User u WHERE u.employeeId = :employeeId")
	User findUserByEmployeeId(@Param("employeeId") String employeeId);
}
