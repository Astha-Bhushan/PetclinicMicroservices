package com.demo.entity ;

import java.util.ArrayList;
import java.util.List;




public class Owner {
	
private int ownerId;
	 private String ownerName;

	
	private List<Pet> pets = new ArrayList<>() ;
	
	public Owner()
	{
		
	}

	public Owner(int ownerId, String ownerName, List<Pet> pets) {

		this.ownerId = ownerId;
		this.ownerName = ownerName;
		this.pets = pets;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public List<Pet> getPets() {
		return pets;
	}

	public void setPets(List<Pet> pets) {
		this.pets = pets;
	}

	public void deletePet() {
		
		pets.removeAll(pets);
		
	}
	
	
	
	

	
}
