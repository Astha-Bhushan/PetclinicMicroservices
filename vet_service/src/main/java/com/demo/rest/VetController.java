package com.demo.rest;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.demo.VetRepository;
import com.demo.SpecialityRepository;
import com.demo.entity.Speciality;
import com.demo.entity.Vet;
import com.demo.entity.dto.VetDto;

import io.micrometer.core.annotation.Timed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@Timed(value="vet.app")
class VetController {

	@Autowired
	VetRepository vetRepo;

	@Autowired 
	SpecialityRepository sepRepo;
	
	static Logger logger = LoggerFactory.getLogger(VetController.class);

//	// 1. Search vet based on speciality -- WORKING
//	@GetMapping(path = "/vet/s/{speciality}", produces = { MediaType.APPLICATION_JSON_VALUE,
//			MediaType.APPLICATION_XML_VALUE })
//
//	public ResponseEntity<Object> findVetBySpeciality(@PathVariable("speciality") String speciality) 
//	{
//
//		Optional<Speciality> sepList = sepRepo.findByName(speciality);
//		
//		if (sepList.isPresent()) {
//			logger.info("Vet with speciality found");
//			return ResponseEntity.ok((sepList.get().getVet()) );
//		} else {
//			logger.info("No vet found for this speciality");
//			return ResponseEntity.status(404).body("{\"status\":\"No vet found for this speciality\"}");
//		}
//
//	}
	
//	 1. Search vet based on speciality id -- WORKING
	@GetMapping(path = "/vet/spec/{sid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> findVetBySpeciality(@PathVariable("sid") int sid) 
	{

		Optional<Speciality> sepList = sepRepo.findById(sid) ;
	
		if (sepList.isPresent()) {
			logger.info("Vet with speciality found");
			return ResponseEntity.ok((sepList.get().getVet()) );
		} else {
			logger.error("No vet found for this speciality");
			return ResponseEntity.status(404).body("{\"status\":\"No vet found for this speciality\"}");
		}

	}
	
	
	

	// (O) Search vet based on id -- WORKING
	@GetMapping(path = "/vet/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<Object> findVetById(@PathVariable("id") int id) {
		Optional<Vet> output = vetRepo.findById(id);
		if (output.isPresent()) {
			logger.info("Vet found");
			return ResponseEntity.ok(output.get());
		} else {
			logger.error("Vet not found");
			return ResponseEntity.status(404).body("{\"status\":\"Vet Not Found\"}");
		}
	}

	// 2. List all vets -- WORKING ( For checking pre existing data)
	@GetMapping(path = "vet/vetList", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Vet>> findVets() {
		logger.info("Vet list found");
		return ResponseEntity.ok(vetRepo.findAll());
	}

	// 9. List all specialities -- WORKING ( For checking pre existing data)
	@GetMapping(path = "vet/specialityList", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Speciality>> findSpecialities() {
		logger.info("Speciality list found");
		return ResponseEntity.ok(sepRepo.findAll());
	}

	// 3. List all vet's for a speciality -- WORKING ( For checking pre existing
	// data)
	@GetMapping(path = "vet/speciality/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> findVetList(@PathVariable("id") int id) {
		if (sepRepo.existsById(id)) {
			Optional<Speciality> specOp = sepRepo.findById(id);
			if (!specOp.isEmpty()) {
				List<Vet> vets = specOp.get().getVet();
				logger.info("Vet with particular speciality found");
				return ResponseEntity.ok(vets);
			} else {
				logger.info("Speciality is null");
				return ResponseEntity.ok("Speciality is null ");
			}

		} else {
			logger.info("Speciality doesnot exist");
			return ResponseEntity.ok("Speciality doesnot exist");
		}

	}

	// 4. Add vet (adds only vet ) -- WORKING (MANDATORY)
	@PostMapping(path = "vet/addVet", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> addVet(@RequestBody VetDto v) {

		if (vetRepo.existsById(v.getVetId())) {
			logger.info("Vet already exists");
			return ResponseEntity.ok(" {\"status\":\"Vet already exists\"} ");
		} 
		else {
			Vet vet = new Vet() ;
			vet.setVetId(v.getVetId());
			vet.setName(v.getName());
			vet.setSpeciality(v.getSpeciality());
			vetRepo.save(vet);
			
			logger.info("Vet saved");
			return ResponseEntity.ok("{ \"status\":\"Vet saved !\" }");

		}

	}

	// 7. Add speciality to vet -- WORKING (MANDATORY )
	@PostMapping(path = "vet/{vetId}/addSpeciality/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> addSpeciality(@PathVariable int vetId, @PathVariable int id) {

		if (vetRepo.existsById(vetId)) {
			if (sepRepo.existsById(id)) {
				Optional<Vet> vetOp = vetRepo.findById(vetId);
				if (!vetOp.isEmpty()) {
					Vet vet = vetOp.get();

					Optional<Speciality> sepOp = sepRepo.findById(id);
					Speciality speciality = sepOp.get();

					List<Speciality> specialityList = vet.getSpeciality();
					specialityList.add(speciality);

					vet.setSpeciality(specialityList);
					vetRepo.save(vet);

					List<Vet> vetList = speciality.getVet();
					vetList.add(vet);

					speciality.setVet(vetList);
					sepRepo.save(speciality);

					logger.info("Speciality saved in vet");
					return ResponseEntity.ok(" Speciality saved in vet !");
				} else {
					
					logger.info("Vet is null");
					return ResponseEntity.ok(" Vet is null");
				}
			} else {
				
				logger.info("Speciality doesn't exist");
				return ResponseEntity.ok(" Speciality doesn't exist !");
			}

		} else {
			logger.error("Vet doesn't exist");
			return ResponseEntity.status(404).body("{\"status\":\" Vet doesn't exist !\"}");

		}

	}

	// 6. Update vet
	@PutMapping(path = "vet/updateVet/{vetId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> updateVet(@PathVariable int vetId, @RequestBody VetDto v) {

		if (vetRepo.existsById(vetId)) 
		{
			Optional<Vet> vetOp = vetRepo.findById(vetId);
			Vet vet = vetOp.get();
			vet.setVetId(v.getVetId());
			vet.setName(v.getName());
			vet.setSpeciality(v.getSpeciality());
			vetRepo.save(vet);
			
			logger.info("Vet's details got updated");
			return ResponseEntity.status(200).body("{\"status\":\"Vet's details got updated\"}");
		} else {

			logger.error("Vet doesnot exist");
			return ResponseEntity.status(404).body("{\"status\":\"Vet doesnot exist\"}");
		}

	}

	// 5. Remove vet -- WORKING
	@DeleteMapping(path = "vet/deleteVet/{vetId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> removeVet(@PathVariable int vetId) {
		if (vetRepo.existsById(vetId)) {
			Optional<Vet> vetOp = vetRepo.findById(vetId);
			Vet vet = vetOp.get();

			vet.setSpeciality(null);
			vetRepo.save(vet);

			vetRepo.deleteById(vetId);

			logger.info("Vet deleted");
			return ResponseEntity.ok("{\"status\":\" Vet deleted !\"}"); // body("{\"status\":\" Vet deleted !\"}");

		} else {

			logger.error("Vet doesn't exist !");
			return ResponseEntity.status(404).body("{\"status\":\" Vet doesn't exist !\"}");
		}

	}

	// 8. Remove speciality from vet -- WORKING

	@DeleteMapping(path = "vet/{vetId}/removeSpeciality/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> removeSpeciality(@PathVariable int vetId, @PathVariable int id) {
		if (vetRepo.existsById(vetId)) {
			if (sepRepo.existsById(id)) {
				Optional<Vet> vetOp = vetRepo.findById(vetId);
				
				Vet vet = vetOp.get();

				Optional<Speciality> sepOp = sepRepo.findById(id);
				Speciality speciality = sepOp.get();

				List<Speciality> specialityList = vet.getSpeciality();
				specialityList.remove(speciality);

				vet.setSpeciality(specialityList);
				vetRepo.save(vet);

				List<Vet> vetList = speciality.getVet();
				vetList.remove(vet);

				speciality.setVet(vetList);
				sepRepo.save(speciality);


				logger.info("Speciality removekd from vet !");
				return ResponseEntity.ok(" Speciality removed from vet !");
			} else {
				
				logger.info("Speciality doesn't exist !");
				return ResponseEntity.ok(" Speciality doesn't exist !");
			}

		} else {
			logger.info("Vet doesn't exist");
			return ResponseEntity.ok(" Vet doesn't exist !");
		}

	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Exception> handleError(Exception ex) {
		return ResponseEntity.ok(ex);
	}

}
