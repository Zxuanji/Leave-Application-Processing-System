package javaCA.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import javaCA.model.User;
import javaCA.model.Role;
import javaCA.service.EmployeeService;
import javaCA.service.RoleService;
import javaCA.service.UserService;
import javaCA.validator.UserValidator;

@Controller
@RequestMapping("/admin/user")
public class AdminUserController {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private UserValidator userValidator;

	@InitBinder
	private void initUserBinder(WebDataBinder binder) {
		binder.addValidators(userValidator);
	}

	@GetMapping("/list")
	public String userListPage(Model model) {
		List<User> userList = userService.findAllUsers();
		model.addAttribute("userList", userList);

		return "user-list";
	}

	@GetMapping("/create")
	public String createUserPage(Model model) {
		model.addAttribute("user", new User());

		List<String> eidList = employeeService.findAllEmployeeIDs();
		model.addAttribute("eidlist", eidList);

		List<Role> roles = roleService.findAllRoles();
		model.addAttribute("roles", roles);

		return "user-new";
	}

	@PostMapping("/create")
	public String createUser(@ModelAttribute @Valid User user, BindingResult result, Model model) {
		if (result.hasErrors()) {
//			model.addAttribute("user", new User());

			List<String> eidList = employeeService.findAllEmployeeIDs();
			model.addAttribute("eidlist", eidList);

			List<Role> roles = roleService.findAllRoles();
			model.addAttribute("roles", roles);

			return "user-new";
		}

		List<Role> newRoleSet = new ArrayList<Role>();
		user.getRoleSet().forEach(role -> {
			Role completeRole = roleService.findRoleById(role.getRoleId());
			newRoleSet.add(completeRole);
		});
		user.setRoleSet(newRoleSet);
		userService.createUser(user);
		return "redirect:/admin/user/list";
	}

	@GetMapping("/edit/{id}")
	public String editUserPage(@PathVariable String id, Model model) {
		User user = userService.findUserById(id);
		model.addAttribute("user", user);

		List<Role> roles = roleService.findAllRoles();
		model.addAttribute("roles", roles);

		return "user-edit";
	}

	@PostMapping("/edit/{id}")
	public String editUser(@ModelAttribute @Valid User user, BindingResult result, Model model,
			@PathVariable String id) {
		if (result.hasErrors()) {
//			User originalUser = userService.findUserById(id);
//			model.addAttribute("user", originalUser);
			
			List<Role> roles = roleService.findAllRoles();
			model.addAttribute("roles", roles);

			return "user-edit";
		}
		userService.changeUser(user);
		return "redirect:/admin/user/list";
	}
	
	@DeleteMapping("/delete/{id}")
	public String deleteUser(@PathVariable String id) {
		User user = userService.findUserById(id);
		userService.removeUser(user);
		
		return "forward:/admin/user/list";
	}
}
