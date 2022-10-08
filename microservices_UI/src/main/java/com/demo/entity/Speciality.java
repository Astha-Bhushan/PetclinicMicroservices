package com.demo.entity;

import java.util.List;


import java.util.ArrayList;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Speciality {

	private int specialityId;
	private String name;

	private List<Vet> vet;

	public Speciality() {

	}

	public Speciality(int specialityId, String name, List<Vet> vet) {

		this.specialityId = specialityId;
		this.name = name;
		this.vet = vet;
	}

	public int getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(int specialityId) {
		this.specialityId = specialityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Vet> getVet() {
		return vet;
	}

	public void setVet(List<Vet> vet) {
		this.vet = vet;
	}
	
	public String toString()
	{
		return  " [ " + specialityId + " : " + name + " ] ";
	}

}
