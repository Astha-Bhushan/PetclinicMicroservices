package com.demo.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.VisitRepository;
import com.demo.entity.Visit;
import com.demo.entity.VisitDto;

import io.micrometer.core.annotation.Timed;

@RestController
@Timed(value="vist.app")
public class VisitController {

	static Logger logger = LoggerFactory.getLogger(VisitController.class);
	
	@Autowired
	VisitRepository repo;

	@Autowired
	RestTemplate rt;
	

	// 2. create a new visit for pet
	@PostMapping(path = "/pets/{id}/createVisit" , produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createVisit(@RequestBody VisitDto v, @PathVariable int id) {
		Visit visit = new Visit() ;
		
		visit.setPetId(id);
		visit.setVisitId(v.getVisitId());
		visit.setVetId(v.getVetId());
		visit.setSpeciality(v.getSpeciality());
		
		
		repo.save(visit);
		
		logger.info("Created visit for pet");
		return ResponseEntity.ok("Created visit for pet !");

	}

	// 3. List all Visits for a pet using pet id;
	@GetMapping("/pets/{id}/visits")
	public ResponseEntity<List<Visit>> visits(@PathVariable int id) {
		
		logger.info("Visit list with given pet id is found");
		return ResponseEntity.ok(repo.findByPetId(id));
	}

	@GetMapping(path = "/visit/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<Object> findVisitById(@PathVariable("id") int id) {
		Optional<Visit> output = repo.findById(id);
		if (output.isPresent()) {
			
			logger.info("Visit found with id");
			return ResponseEntity.ok(output.get());
		} else {
			
			logger.error("Visit Not Found");
			return ResponseEntity.status(404).body("{\"status\":\"Visit Not Found\"}");
		}
	}

	// 4. List all visits for a given set of pet id(s
	@GetMapping(path = "/visit/list/{petId}")
	public List<Visit> visitList(@PathVariable("petId") int petId) {
		
		logger.info("Visit found with pet id");
		return repo.findByPetId(petId);
	}

	@GetMapping(path = "/visit/list/set")
	public List<Visit> findAllByPetId(String[] petIds) {
		List<Integer> idList = Arrays.asList(petIds).stream().map(s -> Integer.parseInt(s))
				.collect(Collectors.toList());
		
		logger.info("All visits found with given set of pet ids");
		return repo.findAllByPetId(idList);
	}

}
