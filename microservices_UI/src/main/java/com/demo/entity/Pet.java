package com.demo.entity;

import java.util.Objects;

public class Pet {


	private int petId;
	private String petName;
	private String type;
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
