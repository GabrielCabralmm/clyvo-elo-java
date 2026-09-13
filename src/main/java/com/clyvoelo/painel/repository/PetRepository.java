package com.clyvoelo.painel.repository;

import com.clyvoelo.painel.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
}
