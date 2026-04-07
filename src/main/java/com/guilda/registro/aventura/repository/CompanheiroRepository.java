package com.guilda.registro.aventura.repository;

import com.guilda.registro.aventura.model.Companheiro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanheiroRepository extends JpaRepository<Companheiro, Long> {
}