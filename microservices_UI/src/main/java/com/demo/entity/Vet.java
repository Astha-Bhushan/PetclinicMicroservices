package com.demo.entity;

import java.util.List;


import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Vet {

	private int vetId;
	private String name;

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
	
	public String toString()
	{
		return vetId + " : " + name ;
	}

}
