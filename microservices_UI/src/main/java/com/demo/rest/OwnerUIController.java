package com.demo.rest;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import com.demo.entity.Owner;
import com.demo.entity.Pet;
import com.demo.entity.Vet;
import com.demo.entity.Visit;


@Controller
@RequestMapping(value="/ui")
public class OwnerUIController {

	@Autowired
	RestTemplate rt;

	// ------------------------------------------ Finding owner

	@GetMapping(path = "find")
	public String getPage() {
		return "find-owner";
	}

	@PostMapping(path = "/find")
	public ModelAndView getOwnerDetail(@RequestParam("ownerId") int id) {
		System.out.println("Inside finder..");
		Owner o = rt.getForObject("http://owner-service/owner/" + id, Owner.class);
		ModelAndView mv = new ModelAndView();
		mv.addObject("ownerData", o);
		System.out.println(o.getOwnerName());
		mv.setViewName("find-owner");
		return mv;
   }


	// ---------------------------------------- Finding pet by owner Id

	@GetMapping(path = "findOnePet2")
	public ModelAndView findOnePet2(@RequestParam("id") int id) {
		Owner pet = rt.getForObject("http://owner-service/owner/" + id + "/pet", Owner.class);
		ModelAndView mv = new ModelAndView();
		mv.addObject("petData", pet);
		mv.setViewName("find-pet");
		return mv;

	}

	@GetMapping(path = "findOnePet")
	public ModelAndView findOnePet(@RequestParam("id") int id) {
		Pet dto = rt.getForObject("http://owner-service/pets/" + id, Pet.class);
		ModelAndView mv = new ModelAndView();
		mv.addObject("petData", dto);
		mv.setViewName("find-pet");
		return mv;
	}

	// ---------------------------------------- Adding owner -- WORKING

	@GetMapping(path = "add")
	public String getAddPage(ModelMap map) {
		Owner owner = new Owner();
		map.addAttribute("owner", owner);
		return "add-owner";
	}

	@PostMapping(path = "/save")
	public ModelAndView saveEmp(@ModelAttribute("owner") Owner o) {
		ModelAndView mv = new ModelAndView();
		HttpEntity req = new HttpEntity(o);
		ResponseEntity<String> resp = rt.exchange("http://owner-service/owner/addOwner", HttpMethod.POST, req,
				String.class);
		mv.setViewName("saved-owner");
		mv.addObject("resp", resp.getBody());
		return mv;
	}
	// ---------------------------------------- Adding pet ----

	@GetMapping(path = "add-pet")
	public String getPetPage(ModelMap map, @RequestParam("id") int ownerId, HttpSession session) {

		Pet pet = new Pet();
		session.setAttribute("ownerId", ownerId);
		map.addAttribute("pet", pet);
		return "add-pet";
	}

	@PostMapping(path = "add-pet")
	public ModelAndView addVisit(@ModelAttribute("pet") Pet p, HttpSession session) {

		Integer ownerid = (Integer) session.getAttribute("ownerId");
		HttpHeaders header = new HttpHeaders() ;
		header.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		HttpEntity req = new HttpEntity(p , header);
		ResponseEntity<String> resp = rt.exchange("http://owner-service/owner/" + ownerid + "/addPet", HttpMethod.POST,
				req, String.class);

		ModelAndView mv = new ModelAndView();

		mv.addObject("pet", resp.getBody());

		mv.setViewName("savePet");
		return mv;
	}
	// ---------------------------------------- Adding speciality ----

	@GetMapping(path = "add-spec")
	public String getVetPage(ModelMap map, @RequestParam("id") int vetId, HttpSession session) {

		Vet vet = new Vet();
		session.setAttribute("vetId", vetId);
		map.addAttribute("vet", vet);
		return "add-spec";
	}

	@PostMapping(path = "add-spec")
	public ModelAndView addSpec(@ModelAttribute("pet") Pet p, HttpSession session) {

		Integer vetid = (Integer) session.getAttribute("ownerId");
		HttpEntity req = new HttpEntity(p);
		ResponseEntity<String> resp = rt.exchange("http://owner-service/vet/" + vetid + "/addPet", HttpMethod.POST,
				req, String.class);

		ModelAndView mv = new ModelAndView();

		mv.addObject("spec", resp.getBody());

		mv.setViewName("savePet");
		return mv;
	}

	// --------------------------------------- Listing owner -- WORKING

	@GetMapping(path = "list")
	public ModelAndView listAll(HttpSession session) {
		ResponseEntity<List<Owner>> ownerList = rt.exchange("http://owner-service/owner/ownerList", HttpMethod.GET,
				null, new ParameterizedTypeReference<List<Owner>>() {
				});

		ModelAndView mv = new ModelAndView();
		mv.addObject("ownerlist", ownerList.getBody());

		mv.setViewName("ownerlist");
		return mv;
	}
	// --------------------------------------- Listing pet -- WORKING

	
	@GetMapping(path = "listPet")
	public ModelAndView listAllPet(@RequestParam("id") int id) {
		ResponseEntity<List<Pet>> petList = rt.exchange("http://owner-service/owner/" + id + "/pet", HttpMethod.GET,
				null, new ParameterizedTypeReference<List<Pet>>() {
				});
		ModelAndView mv = new ModelAndView();
		mv.addObject("petlist", petList.getBody());
		mv.setViewName("pet-list");
		return mv;
	}

	// --------------------------------------- Listing vets -- WORKING

	@GetMapping(path = "listVet")
	public ModelAndView listAllVets() {
		ResponseEntity<List<Vet>> vetList = rt.exchange("http://vet-service/vet/vetList", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Vet>>() {
				});
		ModelAndView mv = new ModelAndView();
		mv.addObject("vetlist", vetList.getBody());
		mv.setViewName("vet-list");
		return mv;
	}

	// ---------------------------------------- Updating owner -- WORKING

	@GetMapping(value = "update")
	public String getUpdatePage(ModelMap map, @RequestParam("id") int id, HttpSession session) {
		session.setAttribute("id", id);
		ResponseEntity<Owner> resp = rt.getForEntity("http://owner-service/owner/" + id, Owner.class);
		map.addAttribute("owner", resp.getBody());

		return "update-owner";
	}

	@PostMapping(path = "update")
	public ModelAndView updateOwner(@ModelAttribute("owner") Owner o, HttpSession session) {
		Integer id = (Integer) session.getAttribute("id");
		ModelAndView mv = new ModelAndView();
		
		HttpHeaders header = new HttpHeaders() ;
		header.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		HttpEntity req = new HttpEntity(o, header);
		
		ResponseEntity<String> resp = rt.exchange("http://owner-service/owner/update/" + id, HttpMethod.PUT, req,
				String.class);
		mv.setViewName("saved-owner");
		mv.addObject("owner", resp.getBody());
		return mv;
	}

	// ---------------------------------------- Deleting owner

	@GetMapping(value = "delete")
	public String getDeletePage(ModelMap map, @RequestParam("id") int id, HttpSession session) {
		session.setAttribute("id", id);
		ResponseEntity<Owner> resp = rt.getForEntity("http://owner-service/owner/" + id, Owner.class);
		map.addAttribute("owner", resp.getBody());

		return "delete-owner";
	}

	@PostMapping(path = "delete")
	public ModelAndView deleteOwner(@ModelAttribute("owner") Owner o, HttpSession session) {
		Integer id = (Integer) session.getAttribute("id");
		ModelAndView mv = new ModelAndView();
		HttpEntity req = new HttpEntity(o);
		ResponseEntity<String> resp = rt.exchange("http://owner-service/owner/deleteOwner/" + id, HttpMethod.DELETE,
				req, String.class);
		mv.setViewName("saved-owner");
		mv.addObject("owner", resp.getBody());
		return mv;
	}

	// ---------------------------------------- Deleting pet ----

	@GetMapping(path = "delete-pet")
	public String deletePet(ModelMap map, @RequestParam("petid") int petId, HttpSession session) {

		Pet pet = new Pet();
		ResponseEntity<Pet> resp = rt.getForEntity("http://owner-service/pet/" + petId, Pet.class);
		map.addAttribute("pet", resp.getBody());
		return "delete-pet";
	}

	@PostMapping(path = "delete-pet")
	public ModelAndView deletePetPost(@ModelAttribute("pet") Pet p, HttpSession session) {

		Integer ownerid = (Integer) session.getAttribute("ownerId");
		Integer petid = (Integer) session.getAttribute("petId");
		HttpEntity req = new HttpEntity(p);
		ResponseEntity<String> resp = rt.exchange("http://owner-service/owner/" + ownerid + "/deletePet/" + petid,
				HttpMethod.DELETE, req, String.class);

		ModelAndView mv = new ModelAndView();

		mv.addObject("pet", resp.getBody());

		mv.setViewName("saved-owner");
		return mv;
	}
	// ----------------------------------------Finding visits for a pet

	@GetMapping(value = "findvisitsforpet")
	public String getVisitList() { // "/pets/{id}/visits")
		return "findvisitsforpet";
	}

	@PostMapping(value = "findvisitsforpet")
	public ModelAndView getAllVisitsForPet(@RequestParam("petid") int petid) {
		ResponseEntity<List<Visit>> visitList = rt.exchange("http://owner-service/pet/" + petid, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Visit>>() {
				});

		ModelAndView mv = new ModelAndView();
		mv.addObject("visitList", visitList.getBody());
		Pet dto;
		List<Pet> p = new ArrayList<>();
		for (Visit v : visitList.getBody()) {
			int id1 = v.getPetId();
			dto = rt.getForObject("http://owner-service/pets/" + id1 + "/visits", Pet.class);
			p.add(dto);
		}
		mv.addObject("petData", p);
		mv.setViewName("findvisitsforpet");
		return mv;
	}

	@GetMapping(path = "visits")
	public String getVisits() {
		return "visits";
	}

//--------------------------------- find visits - WORKING 

	@GetMapping(path = "findVisit")
	public String getVisit() {
		return "find-visit";
	}

	@PostMapping(path = "findVisit")
	public ModelAndView getVisitDetails(@RequestParam("id") int id) {
		ModelAndView mv = new ModelAndView();
		try {
			Visit vr = rt.getForObject("http://visit-service/visit/" + id, Visit.class);

			mv.addObject("visitData", vr);
			mv.setViewName("find-visit");
		} catch (Exception ex) {
			String msg = "Id not found";
			mv.addObject("resp", msg);
			mv.setViewName("visit-not-exist");
		}
		return mv;
	}

//---------------------------------------------------------------------------------------
	@GetMapping(path = "findOneVisit")
	public ModelAndView findOneVisit(@RequestParam("id") int id) {
		Visit vr = rt.getForObject("http://visit-service/visit/" + id, Visit.class);
		ModelAndView mv = new ModelAndView();
		mv.addObject("visitData", vr);
		mv.setViewName("find-visit");
		return mv;
	}

	// --------------------------------------- Listing visits -- WORKING

	@GetMapping(path = "listVisit")
	public ModelAndView listAllVisit(@RequestParam("petid") int petid) {
		ResponseEntity<List<Visit>> visitList = rt.exchange("http://visit-service/pets/" + petid + "/visits",
				HttpMethod.GET, null, new ParameterizedTypeReference<List<Visit>>() {
				});
		ModelAndView mv = new ModelAndView();
		mv.addObject("visitlist", visitList.getBody());
		mv.setViewName("pet-visit-list");
		return mv;
	}
	// ---------------------------------------- Adding visit --- -- WORKING

	@GetMapping(path = "add-visit")
	public String getVisitPage(ModelMap map, @RequestParam("petid") int ownerId, HttpSession session) {
		Visit visit = new Visit();
		session.setAttribute("ownerId", ownerId);
		map.addAttribute("visit", visit);
		return "add-visit";
	}

	@PostMapping(path = "add-visit")
	public ModelAndView addVisit(@ModelAttribute("visit") Visit v, HttpSession session) {

		HttpHeaders header = new HttpHeaders() ;
		header.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		HttpEntity req = new HttpEntity(v, header);
		
		
		
		Integer ownerid = (Integer) session.getAttribute("ownerId");
		ResponseEntity<String> resp = rt.exchange("http://visit-service/pets/" + ownerid + "/createVisit",
				HttpMethod.POST, req, String.class);

		ModelAndView mv = new ModelAndView();

		mv.addObject("resp", resp.getBody());

		mv.setViewName("savePet");
		return mv;
	}	
}
