package com.demo.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;



@Entity
@Table(name="OWNER") 
public class Owner {
	
	@Id@Column(name="OWNERID")private int ownerId;
	 @Column(name="OWNERNAME")private String ownerName;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL , orphanRemoval = true)
	@JoinColumn(name="OWNERID")
	//@JsonIgnore 
	private Set<Pet> pets = new HashSet<>() ;
	
	public Owner()
	{
		
	}

	public Owner(int ownerId, String ownerName, Set<Pet> pets) {

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

	public Set<Pet> getPets() {
		return pets;
	}

	public void setPets(Set<Pet> pets) {
		this.pets = pets;
	}
	
}
