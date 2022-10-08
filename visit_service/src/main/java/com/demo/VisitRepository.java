package com.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.entity.Visit;

public interface VisitRepository extends JpaRepository<Visit, Integer> {

	List<Visit> findByPetId(int petId);

	@Query("select v from Visit v where petId in ?1")
	public List<Visit> findAllByPetId(List<Integer> idlist);

}
