package com.guilda.registro.aventura.repository;

import com.guilda.registro.aventura.model.Aventureiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AventureiroRepository extends JpaRepository<Aventureiro, Long>, AventureiroRepositoryCustom {

    List<Aventureiro> findByOrganizacaoId(Long organizacaoId);

    @Query("SELECT a FROM Aventureiro a LEFT JOIN FETCH a.companheiro WHERE a.id = :id")
    Optional<Aventureiro> findByIdWithCompanheiro(@Param("id") Long id);
}