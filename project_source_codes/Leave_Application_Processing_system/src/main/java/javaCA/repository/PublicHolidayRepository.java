package javaCA.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import javaCA.model.PublicHoliday;

public interface PublicHolidayRepository extends JpaRepository<PublicHoliday, String> {

}
