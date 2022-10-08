package com.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.demo.entity.Owner;

public interface OwnerRepository extends JpaRepository<Owner, Integer>
{

	

}
