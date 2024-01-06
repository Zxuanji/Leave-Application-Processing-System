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
import javaCA.model.Role;
import javaCA.service.RoleService;
import javaCA.validator.RoleValidator;

@Controller
@RequestMapping("/admin/role")
public class AdminRoleController {
	@Autowired
	private RoleService roleService;

	@Autowired
	private RoleValidator roleValidator;

	@InitBinder
	private void initRoleBinder(WebDataBinder binder) {
		binder.addValidators(roleValidator);
	}

	@GetMapping("/list")
	public String roleListPage(Model model) {
		List<Role> roleList = roleService.findAllRoles();
		model.addAttribute("roleList", roleList);

		return "role-list";
	}

	@GetMapping("/create")
	public String createRolePage(Model model) {
		model.addAttribute("role", new Role());

		return "role-new";
	}

	@PostMapping("/create")
	public String createRole(@ModelAttribute @Valid Role role, BindingResult result) {
		if (result.hasErrors())
			return "role-new";

		roleService.createRole(role);
		return "redirect:/admin/role/list";
	}

	@GetMapping("/edit/{id}")
	public String editRolePage(@PathVariable String id, Model model) {
		Role role = roleService.findRoleById(id);
		model.addAttribute("role", role);

		return "role-edit";
	}

	@PostMapping("/edit/{id}")
	public String editRole(@ModelAttribute @Valid Role role, BindingResult result, @PathVariable String id) {
		if (result.hasErrors())
			return "role-edit";
		
		roleService.changeRole(role);
		return "redirect:/admin/role/list";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteRole(@PathVariable String id) {
		Role role = roleService.findRoleById(id);
		roleService.removeRole(role);
		
		return "forward:/admin/role/list";
	}
}
