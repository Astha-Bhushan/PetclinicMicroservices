package com.demo.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;



@Entity
@Table(name="VET")
public class Vet {
	
	@Id@Column(name="VETID")private int vetId;
	 @Column(name="VETNAME")private String name;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "VET_SPECIALITY", joinColumns = { @JoinColumn(name = "VETID") }, inverseJoinColumns = {
			@JoinColumn(name = "SPECIALITYID") })
	private List<Speciality> speciality;

	public Vet() {

	}

	public Vet(int vetId, String name, List<Speciality> speciality) {
		this.vetId = vetId;
		this.name = name;
		this.speciality = speciality;
	}

	public int getVetId() {
		return vetId;
	}

	public void setVetId(int vetId) {
		this.vetId = vetId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Speciality> getSpeciality() {
		return speciality;
	}

	public void setSpeciality(List<Speciality> speciality) {
		this.speciality = speciality;
	}

}
