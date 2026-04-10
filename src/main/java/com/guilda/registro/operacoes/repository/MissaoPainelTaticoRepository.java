package com.guilda.registro.operacoes.repository;

import com.guilda.registro.operacoes.model.MissaoPainelTatico;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface MissaoPainelTaticoRepository extends JpaRepository<MissaoPainelTatico, Long> {

    @Query("SELECT m FROM MissaoPainelTatico m WHERE m.ultimaAtualizacao >= :dataLimite ORDER BY m.indiceProntidao DESC")
    List<MissaoPainelTatico> findTopMissoesUltimosDias(@Param("dataLimite") LocalDateTime dataLimite, Pageable pageable);
}