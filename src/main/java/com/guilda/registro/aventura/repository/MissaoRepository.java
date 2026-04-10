package com.guilda.registro.aventura.repository;

import com.guilda.registro.aventura.model.Missao;
import com.guilda.registro.aventura.model.NivelPerigoEnum;
import com.guilda.registro.aventura.model.StatusMissaoEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface MissaoRepository extends JpaRepository<Missao, Long> {

    List<Missao> findByOrganizacaoId(Long organizacaoId);

    @Query("SELECT m FROM Missao m LEFT JOIN FETCH m.participacoes p LEFT JOIN FETCH p.aventureiro WHERE m.id = :id")
    Optional<Missao> findByIdWithParticipantes(@Param("id") Long id);

    @Query("SELECT m FROM Missao m WHERE " +
            "(:status IS NULL OR m.status = :status) AND " +
            "(:nivelPerigo IS NULL OR m.nivelPerigo = :nivelPerigo) AND " +
            "(:dataInicioInicio IS NULL OR m.dataInicio >= :dataInicioInicio) AND " +
            "(:dataInicioFim IS NULL OR m.dataInicio <= :dataInicioFim)")
    Page<Missao> findWithFilters(@Param("status") StatusMissaoEnum status,
                                 @Param("nivelPerigo") NivelPerigoEnum nivelPerigo,
                                 @Param("dataInicioInicio") LocalDate dataInicioInicio,
                                 @Param("dataInicioFim") LocalDate dataInicioFim,
                                 Pageable pageable);

    @Query("SELECT m FROM Missao m LEFT JOIN m.participacoes p " +
            "WHERE m.createdAt >= :dataInicio " +
            "GROUP BY m.id " +
            "ORDER BY COUNT(p) DESC, m.createdAt DESC")
    List<Missao> findTop10Top15Dias(@Param("dataInicio") OffsetDateTime dataInicio);
}