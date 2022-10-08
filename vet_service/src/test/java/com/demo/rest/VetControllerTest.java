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
import java.util.List;
import java.util.Optional;

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

import com.demo.SpecialityRepository;
import com.demo.VetRepository;
import com.demo.entity.Vet;
import com.demo.entity.Speciality;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
 class VetControllerTest {

	@Autowired
	MockMvc mvc;

	@MockBean
	VetRepository vetRepo;

	@MockBean
	SpecialityRepository sepRepo;

	@MockBean 
	RestTemplate rt;

	@InjectMocks
	VetController vrc;  // left- 8. Remove speciality from vet  

	// ------------------------------1. Search vet based on speciality 

		@Test
		 void testFindBySpec() throws Exception {
			
			List<Speciality> specs = new ArrayList<>() ;
			
			Vet vet = new Vet() ;
			vet.setVetId(12);
			vet.setName("Arun");
			vet.setSpeciality(specs);
			
			when(vetRepo.existsById(12)).thenReturn(true);
			
			List<Vet> vets = new ArrayList<>();
			vets.add(vet);
			
			Speciality spec = new Speciality() ;
			spec.setSpecialityId(15);
			spec.setName("Parasitology");
			spec.setVet(vets);
			
			Mockito.when(sepRepo.findById(15)).thenReturn(Optional.of(spec));
			
			mvc.perform(get("/vet/spec/"+15).accept(MediaType.APPLICATION_JSON))
			        .andExpect(status().isOk()) ;
			    //    .andExpect(jsonPath("$.name").value("Arun")); 
					
		}

		@Test
		 void testFindBySpecNegative() throws Exception {
			
			List<Vet> vet = new ArrayList<>() ;
			when(sepRepo.findById(111)).thenReturn(Optional.of(new Speciality(15, "Parasitology", vet)));
			
			mvc.perform(get("/vet/spec/"+112)).andExpect(content()
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.status").value("No vet found for this speciality"));

		}
		
	// ------------------- 2 . list all vets test
	@Test
	 void testVetList() throws Exception {
		List<Vet> vet = new ArrayList<Vet>();

		Mockito.when(vetRepo.findAll()).thenReturn(vet);

		mvc.perform(get("/vet/vetList").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));

	}

	// ------------------- 9 . list all speciality test
	@Test
	 void testSpecList() throws Exception {
		List<Speciality> spec = new ArrayList<Speciality>();

		Mockito.when(sepRepo.findAll()).thenReturn(spec);

		mvc.perform(get("/vet/specialityList").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));

	}

	// ----------------------- 3. List all vet's for a speciality -- WORKING
	@Test
	 void listAllVetBySpec() throws Exception {

		int id = 403;
		when(sepRepo.existsById(id)).thenReturn(true);

		List<Vet> vet = new ArrayList<Vet>();
		

		mvc.perform(get("/vet/speciality/" + id).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));

	}
	

	// ---------------------- 4. Add new Vet
	@Test
	 void testSaveVet() throws Exception {
		
		Mockito.when(vetRepo.existsById(205)).thenReturn(false);

		List<Speciality> spec = new ArrayList<>() ;
		Vet vet = new Vet();
		vet.setVetId(205);
		vet.setName("Akash");
		vet.setSpeciality(spec);

		when(vetRepo.save(vet)).thenReturn(vet);

		ObjectMapper om = new ObjectMapper();
		String ownerJson = om.writeValueAsString(vet);

		mvc.perform(post("/vet/addVet").contentType(MediaType.APPLICATION_JSON)
				.content(ownerJson))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value("Vet saved !"));

	}

	@Test
	 void testSaveVetNegative() throws Exception {
		Vet vet = new Vet(4, "Karthik", null);

		Mockito.when(vetRepo.existsById(4)).thenReturn(true);

		ObjectMapper om = new ObjectMapper();
		String ownerJson = om.writeValueAsString(vet);

		mvc.perform(post("/vet/addVet").contentType(MediaType.APPLICATION_JSON).content(ownerJson))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value("Vet already exists"));
	}

	
	
	// --------------------------- 6. Update Vet 
	
	@Test
	 void testUpdateVet() throws Exception {

		int id = 203;
		
		Speciality spec = new Speciality(11, "Parasitology", null);
		
		List<Speciality> specs = new ArrayList<Speciality>() ;
		specs.add(spec);
		
		Vet vet  = new Vet(203 , "Rajan" , specs);

		Mockito.when(vetRepo.existsById(203)).thenReturn(true);
        Mockito.when(vetRepo.findById(203)).thenReturn(Optional.of(vet));
		Mockito.when(vetRepo.save(vet)).thenReturn(vet);
		
		ObjectMapper om = new ObjectMapper();
		String ownerJson = om.writeValueAsString(vet);           

		mvc.perform(put("/vet/updateVet/" + id).contentType(MediaType.APPLICATION_JSON).content(ownerJson))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value("Vet's details got updated"));

		// .andExpect(jsonPath("$.ownerName").value("Rani"));

	}
	@Test
	 void testUpdateVetNegative() throws Exception {

		int id = 107 ;
		Vet vet = new Vet() ;
		
		when(vetRepo.existsById(id)).thenReturn(false);
		
		ObjectMapper om = new ObjectMapper();
		String ownerJson = om.writeValueAsString(vet);           

		mvc.perform(put("/vet/updateVet/" + id).contentType(MediaType.APPLICATION_JSON).content(ownerJson))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value("Vet doesnot exist"));

		// .andExpect(jsonPath("$.ownerName").value("Rani"));

	}
	
	// ------------------------------------5. Delete vet 
	
	@Test
	 void shouldDelete() throws Exception {
		int id = 401;
		when(vetRepo.existsById(id)).thenReturn(true);
		
		//Vet vet = vetRepo.findById(401).get() ;
		
	//	Mockito.when(vetRepo.save(vet)).thenReturn(vet) ;

		mvc.perform(delete("/vet/deleteVet/"+id))
		        .andExpect(status().isOk()) ;
				
	}

	@Test
	 void shouldDeleteNegative() throws Exception {
		int id = 401;
		when(vetRepo.existsById(id)).thenReturn(false);

		mvc.perform(delete("/vet/deleteVet/"+id)).andExpect(jsonPath("$.status").value(" Vet doesn't exist !"));

	}
	
	// ------------------------------ 7. Add speciality to vet
	@Test
	 void testSaveSpecByVet() throws Exception {

	//vet exist sep exist vet find sep find
		List<Speciality> specs = new ArrayList<>() ;
		
		Vet vet = new Vet() ;
		vet.setVetId(12);
		vet.setName("Arun");
		vet.setSpeciality(specs);
		
		when(vetRepo.existsById(12)).thenReturn(true);
		
		List<Vet> vets = new ArrayList<>();
		vets.add(vet);
		
		Speciality spec = new Speciality() ;
		spec.setSpecialityId(15);
		spec.setName("Parasitology");
		spec.setVet(vets);
		
		when(sepRepo.existsById(15)).thenReturn(true);
		when(vetRepo.findById(12)).thenReturn(Optional.of(vet)) ;
		when(sepRepo.findById(15)).thenReturn(Optional.of(spec));
		
		ObjectMapper om = new ObjectMapper();
		String petJson = om.writeValueAsString(spec);

		mvc.perform(post("/vet/12/addSpeciality/15").contentType(MediaType.APPLICATION_JSON).content(petJson))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
		// .andExpect(jsonPath("$.petName").value("Billi"));
	}
	
	
	@Test
	 void testSaveSpecByVetNegative() throws Exception {

	//vet exist sep exist vet find sep find
		List<Speciality> specs = new ArrayList<>() ;
		
		Vet vet = new Vet() ;
		vet.setVetId(12);
		vet.setName("Arun");
		vet.setSpeciality(specs);
		
		when(vetRepo.existsById(12)).thenReturn(false);
		
		List<Vet> vets = new ArrayList<>();
		vets.add(vet);
		
		Speciality spec = new Speciality() ;
		spec.setSpecialityId(15);
		spec.setName("Parasitology");
		spec.setVet(vets);
		
		when(sepRepo.existsById(15)).thenReturn(false);
		when(vetRepo.findById(12)).thenReturn(Optional.of(vet)) ;
		when(sepRepo.findById(15)).thenReturn(Optional.of(spec));
		
		ObjectMapper om = new ObjectMapper();
		String petJson = om.writeValueAsString(spec);

		mvc.perform(post("/vet/12/addSpeciality/15").contentType(MediaType.APPLICATION_JSON).content(petJson))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		        .andExpect(jsonPath("$.status").value(" Vet doesn't exist !"));
		// .andExpect(jsonPath("$.petName").value("Billi"));
	}
	
	// --------------------------- 8. Remove speciality from vet 
	
	  @Test

	    void deleteOneSpeciality() throws Exception {
		  List<Speciality> specs = new ArrayList<>() ;
			
			Vet vet = new Vet() ;
			vet.setVetId(12);
			vet.setName("Arun");
			vet.setSpeciality(specs);
			
			when(vetRepo.existsById(12)).thenReturn(false);
			
			List<Vet> vets = new ArrayList<>();
			vets.add(vet);
			
			Speciality spec = new Speciality() ;
			spec.setSpecialityId(15);
			spec.setName("Parasitology");
			spec.setVet(vets);
			
			when(vetRepo.existsById(12)).thenReturn(true);
			when(sepRepo.existsById(15)).thenReturn(true);
			when(vetRepo.findById(12)).thenReturn(Optional.of(vet)) ;
			when(sepRepo.findById(15)).thenReturn(Optional.of(spec));
			
			ObjectMapper om = new ObjectMapper();
			String petJson = om.writeValueAsString(spec);
			
			mvc.perform(delete("/vet/12/removeSpeciality/15"))
			   .andDo(print())
			.andExpect(status().isOk());
			
	    }

	   
	

}
