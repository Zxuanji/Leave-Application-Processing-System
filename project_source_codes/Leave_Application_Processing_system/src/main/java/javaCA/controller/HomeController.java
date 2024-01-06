package javaCA.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import javaCA.model.Employee;
import javaCA.model.User;
import javaCA.model.UserSession;
import javaCA.service.EmployeeService;
import javaCA.service.UserService;

@Controller
@RequestMapping("/home")
public class HomeController {
	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private UserService userService;

	// @GetMapping({"/", "/home"})
	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("user", new User());

		return "login";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

	@PostMapping("/authenticate")
	public String authenticate(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model,
			HttpSession session) {
		if (bindingResult.hasErrors()) return "login";
		
		User newUser = userService.authenticate(user.getName(), user.getPassword());
		if (newUser == null) {
			model.addAttribute("unauthenticated", true);
			return "login";
		}
		
		UserSession userSession = new UserSession();
		userSession.setUser(newUser);
		
		userSession.setEmployee(employeeService.findEmployeeById(newUser.getEmployeeId()));
		
		List<Employee> subordinates = employeeService.findSubordinates(newUser.getEmployeeId());
		if (subordinates != null) userSession.setSubordinates(subordinates);
		
		session.setAttribute("user", userSession);
		
		List<String> roleIds = newUser.getRoleIds();
		
		if (roleIds.contains("admin")) return "redirect:/admin/employee/list";
		
		if (roleIds.contains("manager")) return "redirect:/manager/pending";
		
		return "redirect:/leave/main";
	}
	
	@GetMapping("/noAccess")
	public String noAccess() {
		return "no-access";
	}
}
