package com.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "VISIT")
public class Visit {

	@Id@Column(name = "VISITID")private int visitId;
	     @Column(name = "VETID")private int vetId;   
	  @Column(name="SPECIALITY")private String speciality;
	      @Column(name= "PETID")private int petId;

	public Visit() {

	}

	public Visit(int visitId, int vetId, String speciality, int petId) {

		this.visitId = visitId;
		this.vetId = vetId;
		this.speciality = speciality;
		this.petId = petId;
	}

	public int getVisitId() {
		return visitId;
	}

	public void setVisitId(int visitId) {
		this.visitId = visitId;
	}

	public int getVetId() {
		return vetId;
	}

	public void setVetId(int vetId) {
		this.vetId = vetId;
	}

	public String getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	public int getPetId() {
		return petId;
	}

	public void setPetId(int petId) {
		this.petId = petId;
	}

}
