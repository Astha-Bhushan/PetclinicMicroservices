package com.demo.entity;

import java.util.List;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name="SPECIALITY")
public class Speciality {

	@Id@Column(name="SPECIALITYID")private int specialityId;
  	          @Column(name="NAME")private String name;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "VET_SPECIALITY", joinColumns = { @JoinColumn(name = "SPECIALITYID") }, inverseJoinColumns = {
			@JoinColumn(name = "VETID") })
	@JsonIgnore
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

}
