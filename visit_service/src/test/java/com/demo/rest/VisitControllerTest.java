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

import com.demo.VisitRepository;
import com.demo.entity.Visit;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
 class VisitControllerTest {

	@Autowired
	MockMvc mvc;

	@MockBean
	VisitRepository repo;
	
	@MockBean 
	RestTemplate rt;

	@InjectMocks
	VisitController vrc;  
	
	
	//create new visit test
	@Test
	void testAddVisit() throws Exception
	{
		
		int id = 201 ;
		
		Visit visit = new Visit();
		
		visit.setVisitId(101);
		visit.setPetId(id);
		visit.setVetId(301);
		visit.setSpeciality("Nutrition");
		
        when(repo.save(visit)).thenReturn(visit);

        ObjectMapper om = new ObjectMapper();
		String ownerJson = om.writeValueAsString(visit);

		mvc.perform(post("/pets/"+id+"/createVisit").contentType(MediaType.APPLICATION_JSON)
				.content(ownerJson))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
			//	.andExpect(jsonPath("$.status").value("Created visit for pet !"));		
	}
	
	
	
	// lisit all visit for pet id
	

	@Test
	 void testListVisitById() throws Exception 
	{
		
		
		int petid = 216;
		
		List<Visit> vet = new ArrayList<Visit>();
		

		mvc.perform(get("/pets/" + petid+"/visits").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
		
	}
	
	
	// finding visits 
	
	
	
	
	@Test
	 void testFindById() throws Exception {
		
	  int id = 201 ;
		
		Visit visit = new Visit();
		
		visit.setVisitId(101);
		visit.setPetId(id);
		visit.setVetId(301);
		visit.setSpeciality("Nutrition");
		
		
		Mockito.when(repo.findById(101)).thenReturn(Optional.of(visit));
		
		mvc.perform(get("/visit/"+101).accept(MediaType.APPLICATION_JSON))
		        .andExpect(status().isOk()) ;
		    //    .andExpect(jsonPath("$.name").value("Arun")); 
				
	}
	
	@Test
	 void testFindByIdNegative() throws Exception {
		
		//List<Vet> vet = new ArrayList<>() ;
		when(repo.findById(111)).thenReturn(Optional.of(new Visit(111 , 201 , "Nutrition" , 301)));
		
		mvc.perform(get("/visit/"+112)).andExpect(content().contentType(MediaType.APPLICATION_JSON))
	            .andDo(print())
				.andExpect(jsonPath("$.status").value("Visit Not Found"));

	}
	
	
	// find by pet id
	
	@Test
	 void testFindByPetId() throws Exception {
		
	  int id = 201 ;
		
		Visit visit = new Visit();
		
		visit.setVisitId(101);
		visit.setPetId(id);
		visit.setVetId(301);
		visit.setSpeciality("Nutrition");
		
		List<Visit> visits = new ArrayList<Visit>() ;
		visits.add(visit);
		
		Mockito.when(repo.findByPetId(id)).thenReturn(visits);
		
		mvc.perform(get("/visit/list/"+id).accept(MediaType.APPLICATION_JSON))
		        .andExpect(status().isOk()) ;
		    //    .andExpect(jsonPath("$.name").value("Arun")); 
				
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
	
	
	
	
	
	
