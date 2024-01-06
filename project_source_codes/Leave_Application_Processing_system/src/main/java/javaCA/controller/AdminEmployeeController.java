package javaCA.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import javaCA.model.Employee;
import javaCA.service.EmployeeService;
import javaCA.validator.EmployeeValidator;

@Controller
@RequestMapping("/admin/employee")
public class AdminEmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private EmployeeValidator employeeValidator;

	@InitBinder
	private void initEmployeeBinder(WebDataBinder binder) {
		binder.addValidators(employeeValidator);
	}

	@GetMapping("/list")
	public String employeeListPage(Model model) {
		List<Employee> employeeList = employeeService.findAllEmployees();
		model.addAttribute("employeeList", employeeList);

		return "employee-list";
	}

	@GetMapping("/create")
	public String createEmployeePage(Model model) {
		model.addAttribute("employee", new Employee());
		model.addAttribute("eidlist", employeeService.findAllEmployeeIDs());

		return "employee-new";
	}

	@PostMapping("/create")
	public String createEmployee(@ModelAttribute @Valid Employee employee, BindingResult result, Model model) {

		if (result.hasErrors()) {
			model.addAttribute("eidlist", employeeService.findAllEmployeeIDs()); // without this, after validation will
																					// lose id list
			return "employee-new";
		}

		employeeService.createEmployee(employee);
		return "redirect:/admin/employee/list";
	}

	@GetMapping("/edit/{id}")
	public String editEmployeePage(Model model, @PathVariable String id) {

		Employee employee = employeeService.findEmployeeById(id);
		model.addAttribute("employee", employee);
		model.addAttribute("eidlist", employeeService.findAllEmployeeIDs());

		return "employee-edit";
	}

	@PostMapping("/edit/{id}")
	public String editEmployee(@ModelAttribute @Valid Employee employee, BindingResult result, Model model,
			@PathVariable String id) {
		
		if (result.hasErrors()) {
			model.addAttribute("eidlist", employeeService.findAllEmployeeIDs());
			return "employee-edit";
		}
		
		employeeService.changeEmployee(employee);
		return "redirect:/admin/employee/list";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteEmployee(@PathVariable String id) {
		
		Employee employee = employeeService.findEmployeeById(id);
		employeeService.removeEmployee(employee);
		
		return "forward:/admin/employee/list"; // no need redirect
	}
}
