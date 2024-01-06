package javaCA.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "publicholiday")
public class PublicHoliday {
	
	@Id
	@Column(name = "holidayid")
	private String holidayId;
	
	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;
	
	@Column(name = "fromdate")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate fromDate;
	
	@Column(name = "todate")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate toDate;
	
	public PublicHoliday() {}

	public PublicHoliday(String holidayId, String name, String description, LocalDate fromDate, LocalDate toDate) {
		super();
		this.holidayId = holidayId;
		this.name = name;
		this.description = description;
		this.fromDate = fromDate;
		this.toDate = toDate;
	}

	public String getHolidayId() {
		return holidayId;
	}

	public void setHolidayId(String holidayId) {
		this.holidayId = holidayId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getFromDate() {
		return fromDate;
	}

	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	public LocalDate getToDate() {
		return toDate;
	}

	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}
	
	
}
