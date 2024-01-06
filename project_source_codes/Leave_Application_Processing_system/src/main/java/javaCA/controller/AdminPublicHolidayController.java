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
import javaCA.model.PublicHoliday;
import javaCA.service.PublicHolidayService;
import javaCA.validator.PublicHolidayValidator;

@Controller
@RequestMapping("/admin/holiday")
public class AdminPublicHolidayController {
	@Autowired
	private PublicHolidayService publicHolidayService;

	@Autowired
	private PublicHolidayValidator publicHolidayValidator;

	@InitBinder
	private void initPublicHolidayBinder(WebDataBinder binder) {
		binder.addValidators(publicHolidayValidator);
	}

	@GetMapping("/list")
	public String holidayListPage(Model model) {
		List<PublicHoliday> holidayList = publicHolidayService.findAllHolidays();
		model.addAttribute("holidayList", holidayList);

		return "holiday-list";
	}

	@GetMapping("/create")
	public String createHolidayPage(Model model) {
		model.addAttribute("holiday", new PublicHoliday());

		return "holiday-new";
	}

	@PostMapping("/create")
	public String createHolilday(@ModelAttribute @Valid PublicHoliday holiday, BindingResult result, Model model) {
		if (result.hasErrors()) {
//			model.addAttribute("holiday", holiday);
			return "holiday-new";

		}

		publicHolidayService.createHoliday(holiday);
		return "redirect:/admin/holiday/list";
	}

	@GetMapping("/edit/{id}")
	public String editHolidayPage(@PathVariable String id, Model model) {
		PublicHoliday holiday = publicHolidayService.findHolidayById(id);
		model.addAttribute("holiday", holiday);

		return "holiday-edit";
	}

	@PostMapping("/edit/{id}")
	public String editHoliday(@ModelAttribute @Valid PublicHoliday holiday, BindingResult result, Model model, 
			@PathVariable String id) {
		if (result.hasErrors()) {
//			model.addAttribute("holiday", holiday);
			return "holiday-edit";
		}
			
		
		publicHolidayService.changeHoliday(holiday);
		return "redirect:/admin/holiday/list";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteHolilday(@PathVariable String id) {
		PublicHoliday holiday = publicHolidayService.findHolidayById(id);
		publicHolidayService.removeHolilday(holiday);
		
		return "forward:/admin/holiday/list";
	}
}
