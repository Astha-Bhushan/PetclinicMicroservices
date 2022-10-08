package com.demo.rest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import com.demo.OwnerRepository;
import com.demo.PetRepository;
import com.demo.entity.Owner;
import com.demo.entity.Pet;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
 class OwnerControllerTest {

	@Autowired
	MockMvc mvc;

	@MockBean
	OwnerRepository repo;

	@MockBean
	PetRepository petRepo;

	@MockBean
	RestTemplate rt;

	@InjectMocks
	OwnerController orc;

//	1. An Owner has more than one Pets
//	2. Can create a new owner and add new pets to owner and remove pets from owner
//	3. Can search owner based on its id
//	4. Can list all owners with their pets
//	5. Delete Owner
//	6. Update Owner

	// ---------------------- 1. An Owner has more than one Pets
	// ---------------------- 2. Add new owner
	@Test
	 void testSaveOwner() throws Exception {

		Set<Pet> pets = new HashSet<>();

		Owner o1 = new Owner(107, "Rani", pets);

		pets.add(new Pet(211, "Dolly", "Dog", o1));

		o1.setPets(pets);

		when(repo.save(o1)).thenReturn(o1);

		ObjectMapper om = new ObjectMapper();
		String ownerJson = om.writeValueAsString(o1);

		mvc.perform(post("/owner/addOwner").contentType(MediaType.APPLICATION_JSON).content(ownerJson))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
		// .andExpect(jsonPath("$.ownerName").value("Rani"));

	}

	@Test
	 void testSaveOwnerNegative() throws Exception {
		Owner owners = new Owner(4, "Karthik", null);
		Mockito.when(repo.existsById(4)).thenReturn(true);

		ObjectMapper om = new ObjectMapper();
		String ownerJson = om.writeValueAsString(owners);

		mvc.perform(post("/owner/addOwner").contentType(MediaType.APPLICATION_JSON).content(ownerJson))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value("Owner already exists"));
	}

	// ------------------------------ 2.a. Add pet to owner
	@Test
     void testAddPet()throws Exception{
          Owner od = new Owner(2,"Rinki" , null);
          
          Pet pds = new Pet(6, "Chintu","Dog", od);
          
          when(repo.existsById(2)).thenReturn(true);
          when(repo.save(od)).thenReturn(od);
          when(petRepo.save(pds)).thenReturn(pds);

          ObjectMapper om = new ObjectMapper();
          String petJson = om.writeValueAsString(pds);

           mvc.perform(post("/owner/2/addPet").contentType(MediaType.APPLICATION_JSON).content(petJson))
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON));
      }
	@Test
	 void testAddPetNegative() throws Exception {

		when(repo.existsById(2)).thenReturn(false);
		 Owner od = new Owner(2,"Rinki" , null);
         
         Pet pds = new Pet(6, "Chintu","Dog", od);
         
         ObjectMapper om = new ObjectMapper();
         String petJson = om.writeValueAsString(pds);

		mvc.perform( post("/owner/2/addPet").content(petJson).contentType(MediaType.APPLICATION_JSON) 
				)
				.andExpect(jsonPath("$.status").value("Owner does not exist"));
		
	}
	
	//------------------------------ 2.b. Delete pet from owner
	@Test 
    void testDeletePet()throws Exception{
		
		Owner owner = new Owner();
		Pet pet = new Pet (213 , "chiku","dog" , owner);
		
	
		owner.setOwnerId(1);
		owner.setOwnerName("Rinku");
		owner.setPets(null);
		when(repo.findById(1)).thenReturn(Optional.of(owner));
		
		int petid = 213 ;
		when(petRepo.existsById(petid)).thenReturn(true);
		
		mvc.perform(delete("/owner/1/deletePet/213"))
		        .andExpect(status().isOk());
		        
	}
	
	@Test 
    void testDeletePetNegative()throws Exception{
		
		Owner owner = new Owner();
		Pet pet = new Pet (213 , "chiku","dog" , owner);
		
	
		owner.setOwnerId(1);
		owner.setOwnerName("Rinku");
		owner.setPets(null);
		when(repo.findById(1)).thenReturn(Optional.of(owner));
		
		int petid = 213 ;
		when(petRepo.existsById(petid)).thenReturn(true);
		
		mvc.perform(delete("/owner/2/deletePet/213")).andExpect(jsonPath("$.status").value("owner with id not found"));
		
		        
	}
	
	
	

	// -------------------------- 3. Search owner based on id
	@Test
	 void testFindById() throws Exception {

		Set<Pet> pets = new HashSet<>();

		Owner o1 = new Owner(104, "Niya Sharma", pets);

		pets.add(new Pet(206, "Kobra", "Snake", o1));
		o1.setPets(pets);

		Mockito.when(repo.findById(104)).thenReturn(Optional.of(o1));

		mvc.perform(get("/owner/104").accept(MediaType.APPLICATION_JSON))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.ownerName").value("Niya Sharma"));

	}

	// ----------------------- 3.b Search owner based on id negative
	@Test
	 void testFindByIdNegative() throws Exception {
		when(repo.findById(111)).thenReturn(Optional.of(new Owner(111, "Sunny", null)));
		mvc.perform(get("/owner/101")).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value("Owner Not Found"));

	}
	// ----------------------- 4. Can list all owners with their pets
	
	@Test
	 void testFindAll() throws Exception {
		List<Owner> owner = new ArrayList<Owner>();

		Mockito.when(repo.findAll()).thenReturn(owner);

		mvc.perform(get("/owner/ownerList").accept(MediaType.APPLICATION_JSON))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));

	}

	// --------------------------- 5. Delete owner

	@Test
	 void shouldDelete() throws Exception {
		
		Owner owner = new Owner();
		List<Pet> pets = new ArrayList<Pet>() ;
			
		Pet pet = new Pet (213 , "chiku","dog" , owner);
		pets.add(pet) ;

		owner.setOwnerId(1);
		owner.setOwnerName("RAJIV");
		owner.setPets(null);
		
		when(repo.existsById(1)).thenReturn(true);
		when(repo.findById(1)).thenReturn(Optional.of(owner));
		

		mvc.perform(delete("/owner/deleteOwner/1")).andExpect(status().isOk()) ;
				

	}

	@Test
	 void shouldDeleteNegative() throws Exception {
		
		int id = 101;
		when(repo.existsById(id)).thenReturn(false);

		mvc.perform(delete("/owner/deleteOwner/101")).andExpect(jsonPath("$.status").value("Owner doesnot exist"));

	}

	// --------------------------------- 6. Update owner

	@Test
	 void testUpdateOwner() throws Exception {

		int id = 107;
		when(repo.existsById(id)).thenReturn(true);

		Owner owner = new Owner();
		owner.setOwnerId(id);
		owner.setOwnerName("Rani");
		owner.setPets(null);

		Mockito.when(repo.save(owner)).thenReturn(owner);

		ObjectMapper om = new ObjectMapper();
		String ownerJson = om.writeValueAsString(owner);

		mvc.perform(put("/owner/update/" + id).contentType(MediaType.APPLICATION_JSON).content(ownerJson))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));

		// .andExpect(jsonPath("$.status").value("Rani"));

	}
	
	@Test
	 void testUpdateOwnerNegative() throws Exception {

		int id = 107;
		Owner owner = new Owner() ;
		ObjectMapper om = new ObjectMapper();
		String ownerJson = om.writeValueAsString(owner);
		
		when(repo.existsById(id)).thenReturn(false);

		mvc.perform(put("/owner/update/" + id).contentType(MediaType.APPLICATION_JSON).content(ownerJson))
		   .andDo(print())
		   .andExpect(jsonPath("$.status").value("Owner doesnot exist"));
		  

		// .andExpect(jsonPath("$.status").value("Rani"));

	}

	

	// (O) List all pets
	@Test
	 void testFindAllPet() throws Exception {
		List<Pet> pet = new ArrayList<Pet>();

		Mockito.when(petRepo.findAll()).thenReturn(pet);

		mvc.perform(get("/owner/petlist").accept(MediaType.APPLICATION_JSON))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));

	}

	// (O) search pet by owner -- not giving proper result
//	@Test
//	 void testFindPetByOwnerNegative() throws Exception {
//
//		Set<Pet> pets = new HashSet<>();
//
//		Owner o1 = new Owner(104, "Niya Sharma", pets);
//		Pet pet = new Pet(206, "Kobra", o1, new PetType(3, "Snake", null));
//
//		pets.add(pet);
//		o1.setPets(pets);
//
//		petRepo.save(pet);
//
//		Mockito.when(repo.findById(104)).thenReturn(Optional.of(o1)); // find owner
//
//		mvc.perform(get("/pet/206").accept(MediaType.APPLICATION_JSON))
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
//
//	}

}
