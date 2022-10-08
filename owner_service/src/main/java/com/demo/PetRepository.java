package com.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import com.demo.entity.Pet;

public interface PetRepository extends JpaRepository<Pet , Integer>
{



}
