package com.demo.rest;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.demo.OwnerRepository;
import com.demo.PetRepository;
import com.demo.entity.Owner;
import com.demo.entity.OwnerDto;
import com.demo.entity.Pet;
import com.demo.entity.PetDto;

import io.micrometer.core.annotation.Timed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@Timed(value="owner.app")
public class OwnerController {

	@Autowired
	OwnerRepository repo;

	@Autowired
	PetRepository petRepo;
	
	static Logger logger = LoggerFactory.getLogger(OwnerController.class);

	// // 4. Can list all owners with their pets --- WORKING
	@GetMapping(value = "owner/ownerList", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Owner>> findAll() {
		
		logger.info("Owner list found");
		return ResponseEntity.ok(repo.findAll());
	}

	// (O) 4.b Gives pet list
	@GetMapping(value = "/owner/petlist", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Pet>> findAllPet() {

		logger.info("Pet list found");
		return ResponseEntity.ok(petRepo.findAll());

	}

	// 1. An Owner has more than one Pets
	// 2. Add new owner --- WORKING ( add owner ) (just adds owner not pets )
	@PostMapping(value = "owner/addOwner", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> addOwner(@RequestBody OwnerDto o) {

		if (repo.existsById(o.getOwnerId())) {
			
			logger.info("Owner already exists");
			return ResponseEntity.ok("{\"status\":\"Owner already exists\"}");
		} else {
			Owner owner = new Owner();
			owner.setOwnerId(o.getOwnerId());
			owner.setOwnerName(o.getOwnerName());
			owner.setPets(o.getPets());
			repo.save(owner);
			
			logger.info("Owner details are added");
			return ResponseEntity.ok("{\"status\":\"Owner details are added..\"}");

		}

	}

	// 2.a Add pet to owner --- WORKING

	@PostMapping(value = "/owner/{id}/addPet", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> addPet(@PathVariable int id, @RequestBody PetDto p) {

		if (repo.existsById(id)) {
			Owner owner = repo.findById(id).get();

			Pet pet = new Pet();
			pet.setPetId(p.getPetId());
			pet.setPetName(p.getPetName());
			pet.setType(p.getType());
			pet.setOwners(p.getOwners());

			petRepo.save(pet);
			owner.getPets().add(pet);
			repo.save(owner);

			logger.info("Pet Saved");
			return ResponseEntity.ok("{\"status\":\"Pet Saved\"}");
		} else {

			logger.error("Owner does not exist");
			return ResponseEntity.status(404).body("{\"status\":\"Owner does not exist\"}");
		}

	}

	// 2.b Delete pet from owner --- WORKING

	@DeleteMapping(value = "owner/{ownerid}/deletePet/{petid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deletePet(@PathVariable("ownerid") int id1, @PathVariable("petid") int id2) {
		Optional<Owner> ownerOp = repo.findById(id1);
		if (ownerOp.isPresent())
		{
			Set<Pet> pets = ownerOp.get().getPets();
			for (Pet p : pets)
			{
				if (p.getPetId() == id2) 
				{

					petRepo.deleteById(p.getPetId());

					ownerOp.get().getPets().remove(p);
					break;
				} else 
				{

					return ResponseEntity.ok("{\"status\":\"Pet Not found\"}");
				}
			}
			repo.save(ownerOp.get());
			
			logger.info("Pet Deleted");
			return ResponseEntity.ok("{\"status\":\"Pet Deleted\"}");
		} else {
			
			logger.error("owner with id not found");
			return ResponseEntity.status(404).body("{\"status\":\"owner with id not found\"}");
		}
	}
	// 3. Search owner --- WORKING

	@GetMapping(path = "/owner/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> findOwnerById(@PathVariable("id") int id) {
		Optional<Owner> output = repo.findById(id);
		if (output.isPresent()) {
			
			logger.info("Owner found with given id");
			return ResponseEntity.ok(output.get());
		} else {
			
			logger.info("Owner not found with given id");
			return ResponseEntity.status(404).body("{\"status\":\"Owner Not Found\"}");
		}
	}

	// 3.a. Search pet --- WORKING

	@GetMapping(path = "/pet/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Object> findPet(@PathVariable("id") int id) {
		Optional<Pet> output = petRepo.findById(id);
		if (output.isPresent()) {
			
			logger.info("Pet found with given id");
			return ResponseEntity.ok(output.get());
		} else {
			
			logger.error("Pet not found with given id");
			return ResponseEntity.status(404).body("{\"status\":\"Pet Not Found\"}");
		}
	}

	// (O) 3.b. Search pet by owner id --- WORKING

	@GetMapping(path = "/owner/{id}/pet", produces = { MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Object> findPetById(@PathVariable("id") int id) {
		Optional<Owner> output = repo.findById(id);
		if (output.isPresent()) {
			
			logger.info("Pet found with given owner id");
			return ResponseEntity.ok(output.get().getPets());
		} else {
			
			logger.info("Owner not found");
			return ResponseEntity.status(404).body("{\"status\":\"Owners Not Found\"}");
		}
	}

	// 5. Delete owner -- WORKING

	@DeleteMapping(path = "owner/deleteOwner/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deleteOwner(@PathVariable("id") int id) {
		if (repo.existsById(id)) {
			Optional<Owner> ownerOp = repo.findById(id);

			if (ownerOp.isPresent()) {

				repo.deleteById(id);

				logger.info("Owner deleted");
				return ResponseEntity.ok("{\"status\":\"Owner deleted\"}");
			} else {
				
				logger.error("No such element exception");
				return ResponseEntity.status(404).body("{\"status\":\"No such element exception \"}");
			}
		} else {

			logger.info("Owner doesnot exist");
			return ResponseEntity.status(404).body("{\"status\":\"Owner doesnot exist\"}");
		}

	}

	// 6. Update Owner -- WORKING

	@PutMapping(value = "/owner/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> updateOwner(@RequestBody OwnerDto o, @PathVariable int id) {// extract from json as an
																								// obj

		if (repo.existsById(o.getOwnerId())) {
			Owner owner = new Owner();
			owner.setOwnerId(o.getOwnerId());
			owner.setOwnerName(o.getOwnerName());
			owner.setPets(o.getPets());

			Set<Pet> petList = owner.getPets();
			for (Pet p : petList) {
				p.setOwners(owner);
			}
			repo.save(owner);

			logger.info("Owner updated and saved");
			return ResponseEntity.ok("{\"status\":\"Owner updated and saved\"}");
		} else {
			
			logger.info("Owner doesnot exist");
			return ResponseEntity.ok("{\"status\":\"Owner doesnot exist\"}");

		}
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Exception> handleError(Exception ex) {
		return ResponseEntity.ok(ex);
	}

}
