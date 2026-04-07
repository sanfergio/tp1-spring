package com.guilda.registro.aventura.repository;

import com.guilda.registro.aventura.model.Missao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MissaoRepository extends JpaRepository<Missao, Long> {

    List<Missao> findByOrganizacaoId(Long orgId);

}