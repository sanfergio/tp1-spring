package com.guilda.registro.aventura.repository;

import com.guilda.registro.aventura.model.ParticipacaoMissao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ParticipacaoMissaoRepository extends JpaRepository<ParticipacaoMissao, Long> {
    Optional<ParticipacaoMissao> findByMissaoIdAndAventureiroId(Long missaoId, Long aventureiroId);
    boolean existsByMissaoIdAndAventureiroId(Long missaoId, Long aventureiroId);
}