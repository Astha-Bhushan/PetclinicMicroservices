package com.demo;
import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.entity.Vet;

public interface VetRepository extends JpaRepository<Vet , Integer>
{



}
