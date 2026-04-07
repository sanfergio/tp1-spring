package com.guilda.registro.audit.repository;

import com.guilda.registro.audit.model.Organizacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizacaoRepository extends JpaRepository<Organizacao, Long> {
}