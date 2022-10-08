package com.demo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.demo.entity.Speciality;


public interface SpecialityRepository extends JpaRepository<Speciality , Integer>
{

	Optional<Speciality> findByName(String speciality);

}
