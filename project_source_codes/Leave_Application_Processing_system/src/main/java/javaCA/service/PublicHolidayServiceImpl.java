package javaCA.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import javaCA.model.PublicHoliday;
import javaCA.repository.PublicHolidayRepository;

@Service
@Transactional(readOnly = true)
public class PublicHolidayServiceImpl implements PublicHolidayService {
	@Resource
	private PublicHolidayRepository publicHolidayRepository;
	
	@Override
	public List<PublicHoliday> findAllHolidays(){
		return publicHolidayRepository.findAll();
	}
	
	@Override
	public PublicHoliday findHolidayById(String id) {
		return publicHolidayRepository.findById(id).orElse(null);
	}
	
	@Override
	@Transactional(readOnly = false)
	public PublicHoliday createHoliday(PublicHoliday holiday) {
		return publicHolidayRepository.saveAndFlush(holiday);
	}
	
	@Override
	@Transactional(readOnly = false)
	public PublicHoliday changeHoliday(PublicHoliday holiday) {
		return publicHolidayRepository.saveAndFlush(holiday);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void removeHolilday(PublicHoliday holiday) {
		publicHolidayRepository.delete(holiday);
	}
}
