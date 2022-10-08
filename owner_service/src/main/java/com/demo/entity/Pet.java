package com.demo.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "PET")
public class Pet {

	@Id
	@Column(name = "PETID")
	private int petId;
	@Column(name = "PETNAME")
	private String petName;
	@Column(name = "type")
	private String type;

	@ManyToOne
	@JoinColumn(name = "OWNERID")
	@JsonIgnore
	private Owner owners;

	public Pet() {

	}

	public Pet(int petId, String petName, String type, Owner owners) {
		super();
		this.petId = petId;
		this.petName = petName;
		this.type = type;
		this.owners = owners;
	}

	public int getPetId() {
		return petId;
	}

	public void setPetId(int petId) {
		this.petId = petId;
	}

	public String getPetName() {
		return petName;
	}

	public void setPetName(String petName) {
		this.petName = petName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Owner getOwners() {
		return owners;
	}

	public void setOwners(Owner owners) {
		this.owners = owners;
	}

	@Override
	public int hashCode() {
		return Objects.hash(petId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pet other = (Pet) obj;
		return petId == other.petId;
	}

}
