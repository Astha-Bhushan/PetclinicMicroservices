package com.demo.entity;



public class Visit {

	private int visitId;
	private int vetId;
	private String speciality;
	private int petId;

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
