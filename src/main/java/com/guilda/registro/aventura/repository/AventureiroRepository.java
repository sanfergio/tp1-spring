package com.guilda.registro.aventura.repository;

import com.guilda.registro.aventura.model.Aventureiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface AventureiroRepository extends JpaRepository<Aventureiro, Long> {
    List<Aventureiro> findByOrganizacaoIdAndAtivoTrue(Long orgId);

    @Query("SELECT a FROM Aventureiro a LEFT JOIN FETCH a.companheiro WHERE a.id = :id")
    java.util.Optional<Aventureiro> findByIdWithCompanheiro(Long id);

    List<Aventureiro> findByOrganizacaoId(Long orgId);
}