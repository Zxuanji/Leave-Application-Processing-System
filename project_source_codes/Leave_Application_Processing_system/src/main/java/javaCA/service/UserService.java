package javaCA.service;

import java.util.List;

import javaCA.model.User;

public interface UserService {
	User authenticate(String username, String password);

	List<User> findAllUsers();

	User createUser(User user);

	User changeUser(User user);

	User findUserById(String id);

	User findUserByEmployeeId(String employeeId);

	void removeUser(User user);

}
