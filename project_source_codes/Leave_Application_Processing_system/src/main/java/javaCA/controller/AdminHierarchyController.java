package javaCA.controller;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import javaCA.service.EmployeeService;
import javaCA.service.UserService;
import javaCA.model.Employee;
import javaCA.model.User;

@Controller
@RequestMapping("/hierarchy")
public class AdminHierarchyController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private EmployeeService employeeService;

	@GetMapping("/edit")
	public String editHierarchyPage(Model model) {
		Map<String, Boolean> map = new HashMap<>();
		List<User> allUser = userService.findAllUsers();
		allUser.forEach(user -> {
			map.put(user.getEmployeeId() + " " + user.getName(), user.getRoleIds().contains("manager"));
		});
		model.addAttribute("map", map);

		return "hierarchy-edit";
	}

	@PostMapping("/edit")
	public String editHierarchy(// HttpServletRequest request,
			Model model, String[] selectedEmployees, String selectedManager) {

//		Enumeration<String> parameterNames = request.getParameterNames();
//	    while (parameterNames.hasMoreElements()) {
//	        String paramName = parameterNames.nextElement();
//	        String[] paramValues = request.getParameterValues(paramName);
//	        System.out.println(paramName + ": " + Arrays.toString(paramValues));
//	    }
		
		String managerId = selectedManager.split(" ")[0];
		for (String selectedEmployee : selectedEmployees) {
			String employeeId = selectedEmployee.split(" ")[0];
			Employee employee = employeeService.findEmployeeById(employeeId);
			employee.setManagerId(managerId);
			employeeService.changeEmployee(employee);
		}
		
		return "redirect:/admin/employee/list";
	}
}
