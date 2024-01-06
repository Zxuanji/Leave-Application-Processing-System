package javaCA;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javaCA.model.Category;
import javaCA.model.Employee;
import javaCA.model.Leave;
import javaCA.model.LeaveStatusEnum;
import javaCA.model.PublicHoliday;
import javaCA.model.Role;
import javaCA.model.User;
import javaCA.repository.CategoryRepository;
import javaCA.repository.EmployeeRepository;
import javaCA.repository.LeaveRepository;
import javaCA.repository.PublicHolidayRepository;
import javaCA.repository.RoleRepository;
import javaCA.repository.UserRepository;

@SpringBootApplication
public class JavaCaApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaCaApplication.class, args);
	}

	@Bean
	CommandLineRunner loadData(UserRepository userRepository, EmployeeRepository employeeRepository,
			RoleRepository roleRepository, CategoryRepository categoryRepository, LeaveRepository leaveRepository, PublicHolidayRepository publicHolidayRepository) {
		return (args) -> {
			Role adminRole = roleRepository.save(new Role("admin", "Administrator", "System administrator"));
			Role staffRole = roleRepository.save(new Role("staff", "Staff", "Staff members"));
			Role managerRole = roleRepository.save(new Role("manager", "Manager", "Manager"));

			employeeRepository.save(new Employee("101034", "Admin"));
			employeeRepository.save(new Employee("100027", "Esther Tan"));
			employeeRepository.save(new Employee("312025", "Nguyen Tri Tin", "100027"));
			employeeRepository.save(new Employee("310017", "Cher Wah", "100027"));
			

			User admin = new User("admin", "admin", "password", "101034");
			User esther = new User("esther", "esther", "password", "100027");
			User tin = new User("tin", "tin", "password", "312025");
			User cherwah = new User("cherwah", "cherwah", "password", "310017");

			admin.setRoleSet(Arrays.asList(adminRole));
			esther.setRoleSet(Arrays.asList(staffRole, managerRole));
			tin.setRoleSet(Arrays.asList(staffRole));
			cherwah.setRoleSet(Arrays.asList(staffRole));

			esther.setAnnualLeaveUsed(5);
			userRepository.saveAndFlush(admin);
			userRepository.saveAndFlush(esther);
			userRepository.saveAndFlush(tin);
			userRepository.saveAndFlush(cherwah);

			Category category1 = new Category("annual", "Annual", "Annual Leave claimable in full day", 18);
			Category category2 = new Category("medical", "Medical", "Medical Leave claimable in full day", 60);
			Category category3 = new Category("compensation", "Compensation", "Compensation Leave claimable in half day", 0);
			category1.setDifferenceForAdmin(4);
			
			categoryRepository.saveAndFlush(category1);
			categoryRepository.saveAndFlush(category2);
			categoryRepository.saveAndFlush(category3);

			LocalDate fromDate = LocalDate.of(2023,12,18);
			LocalDate toDate = LocalDate.of(2023,12,22);

			Leave leave1 = new Leave("312025", fromDate, toDate, "Clear leave", "Handover to team", "87651234", category1, LeaveStatusEnum.APPLIED);
			Leave leave2 = new Leave("310017", fromDate, toDate, "Health Checkup", "Handover to team", "92340000", category2, LeaveStatusEnum.UPDATED);
			Leave leave3 = new Leave("100027", fromDate, toDate, "Reservist", "Handover to team", "80128345", category3, LeaveStatusEnum.REJECTED);
			Leave leave4 = new Leave("312025", fromDate, toDate, "Study", "Handover to team", "87651234", category1, LeaveStatusEnum.APPROVED);
			Leave leave5 = new Leave("310017", fromDate, toDate, "Medical Appointment", "Handover to team", "92340000", category2, LeaveStatusEnum.CANCELLED);
			Leave leave6 = new Leave("100027", fromDate, toDate, "Birthday", "Handover to team", "80128345", category3, LeaveStatusEnum.DELETED);
			leave1.setNumOfDays(5);
			leave2.setNumOfDays(5);
			leave4.setNumOfDays(5);

			leaveRepository.saveAndFlush(leave1);
			leaveRepository.saveAndFlush(leave2);
			leaveRepository.saveAndFlush(leave3);
			leaveRepository.saveAndFlush(leave4);
			leaveRepository.saveAndFlush(leave5);
			leaveRepository.saveAndFlush(leave6);

			LocalDate xmas = LocalDate.of(2023,12,25);
			PublicHoliday ph1 = new PublicHoliday("1","Christmas","Christmas Day",xmas,xmas);
			publicHolidayRepository.saveAndFlush(ph1);

		};
	}
}
