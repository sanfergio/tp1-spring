package com.guilda.registro.aventura.service;

import com.guilda.registro.aventura.controller.MissaoMetricasResponse;
import com.guilda.registro.aventura.controller.RankingResponse;
import com.guilda.registro.aventura.model.StatusMissaoEnum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class RelatorioService {

    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true)
    public List<RankingResponse> rankingParticipacao(LocalDate dataInicio, LocalDate dataFim, StatusMissaoEnum statusMissao) {
        String jpql = "SELECT new com.guilda.registro.aventura.controller.RankingResponse(" +
                "a.id, a.nome, COUNT(p), COALESCE(SUM(p.recompensaOuro), 0), SUM(CASE WHEN p.destaque = true THEN 1 ELSE 0 END)) " +
                "FROM ParticipacaoMissao p " +
                "JOIN p.aventureiro a " +
                "WHERE (:dataInicio IS NULL OR p.registeredAt >= :dataInicio) " +
                "AND (:dataFim IS NULL OR p.registeredAt <= :dataFim) " +
                "AND (:status IS NULL OR p.missao.status = :status) " +
                "GROUP BY a.id, a.nome " +
                "ORDER BY COUNT(p) DESC";

        Query query = em.createQuery(jpql, RankingResponse.class);
        query.setParameter("dataInicio", dataInicio);
        query.setParameter("dataFim", dataFim);
        query.setParameter("status", statusMissao);

        @SuppressWarnings("unchecked")
        List<RankingResponse> result = query.getResultList();
        return result;
    }

    @Transactional(readOnly = true)
    public List<MissaoMetricasResponse> relatorioMissoesMetricas(LocalDate dataInicio, LocalDate dataFim) {
        String jpql = "SELECT new com.guilda.registro.aventura.dto.response.MissaoMetricasResponse(" +
                "m.id, m.titulo, m.status, m.nivelPerigo, COUNT(p), COALESCE(SUM(p.recompensaOuro), 0)) " +
                "FROM Missao m " +
                "LEFT JOIN m.participacoes p " +
                "WHERE m.createdAt >= :dataInicio AND m.createdAt <= :dataFim " +
                "GROUP BY m.id, m.titulo, m.status, m.nivelPerigo " +
                "ORDER BY m.createdAt DESC";

        Query query = em.createQuery(jpql, MissaoMetricasResponse.class);
        query.setParameter("dataInicio", dataInicio);
        query.setParameter("dataFim", dataFim);

        @SuppressWarnings("unchecked")
        List<MissaoMetricasResponse> result = query.getResultList();
        return result;
    }
}