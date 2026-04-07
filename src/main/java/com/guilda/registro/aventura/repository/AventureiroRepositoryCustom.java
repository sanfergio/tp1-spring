package com.guilda.registro.aventura.repository;

import com.guilda.registro.aventura.model.Aventureiro;
import com.guilda.registro.aventura.model.ClasseEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AventureiroRepositoryCustom {
    Page<Aventureiro> findWithFilters(Boolean ativo, ClasseEnum classe, Integer nivelMinimo, Pageable pageable);
    Page<Aventureiro> searchByNome(String nome, Pageable pageable);
}