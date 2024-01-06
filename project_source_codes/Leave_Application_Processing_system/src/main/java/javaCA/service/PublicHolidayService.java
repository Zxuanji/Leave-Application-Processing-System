package javaCA.service;

import java.util.List;

import javaCA.model.PublicHoliday;

public interface PublicHolidayService {
	List<PublicHoliday> findAllHolidays();
	PublicHoliday createHoliday(PublicHoliday holiday);
	PublicHoliday findHolidayById(String id);
	PublicHoliday changeHoliday(PublicHoliday holiday);
	void removeHolilday(PublicHoliday holiday);
}
