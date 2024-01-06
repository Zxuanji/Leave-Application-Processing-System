package javaCA.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import javaCA.model.User;
import javaCA.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
	@Resource
	private UserRepository userRepository;

	@Override
	public User authenticate(String username, String password) {
		return userRepository.findUserByNamePwd(username, password);
	}

	@Override
	public List<User> findAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public User findUserById(String id) {
		return userRepository.findById(id).orElse(null);
	}

	@Override
	public User findUserByEmployeeId(String employeeId){
		return userRepository.findUserByEmployeeId(employeeId);
	}

	@Override
	@Transactional(readOnly = false)
	public User createUser(User user) {
		return userRepository.saveAndFlush(user);
	}

	@Override
	@Transactional(readOnly = false)
	public User changeUser(User user) {
		return userRepository.saveAndFlush(user);
	}

	@Override
	@Transactional(readOnly = false)
	public void removeUser(User user) {
		userRepository.delete(user);
	}

}
