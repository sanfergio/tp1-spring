package com.guilda.registro.aventura.service;

import com.guilda.registro.aventura.controller.MissaoDetalhadaResponse;
import com.guilda.registro.aventura.controller.MissaoResumoResponse;
import com.guilda.registro.aventura.controller.ParticipacaoDetalheResponse;
import com.guilda.registro.aventura.model.Missao;
import com.guilda.registro.aventura.model.NivelPerigoEnum;
import com.guilda.registro.aventura.model.ParticipacaoMissao;
import com.guilda.registro.aventura.model.StatusMissaoEnum;
import com.guilda.registro.aventura.repository.MissaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissaoConsultaService {

    private final MissaoRepository missaoRepository;

    @Transactional(readOnly = true)
    public Page<MissaoResumoResponse> listarMissoes(StatusMissaoEnum status, NivelPerigoEnum nivelPerigo,
                                                    LocalDate dataInicioInicio, LocalDate dataInicioFim,
                                                    Pageable pageable) {
        Page<Missao> page = missaoRepository.findWithFilters(status, nivelPerigo, dataInicioInicio, dataInicioFim, (Pageable) pageable);
        return page.map(this::toResumoResponse);
    }

    @Transactional(readOnly = true)
    public MissaoDetalhadaResponse detalharMissao(Long id) {
        Missao missao = missaoRepository.findByIdWithParticipantes(id)
                .orElseThrow(() -> new RuntimeException("Missão não encontrada"));

        var participantes = missao.getParticipacoes().stream()
                .map(this::toParticipacaoDetalhe)
                .collect(Collectors.toList());

        return new MissaoDetalhadaResponse(
                missao.getId(),
                missao.getTitulo(),
                missao.getNivelPerigo(),
                missao.getStatus(),
                missao.getDataInicio(),
                missao.getDataTermino(),
                missao.getOrganizacao().getId(),
                missao.getCreatedAt(),
                participantes
        );
    }

    private MissaoResumoResponse toResumoResponse(Missao m) {
        return new MissaoResumoResponse(
                m.getId(), m.getTitulo(), m.getStatus(), m.getNivelPerigo(),
                m.getDataInicio(), m.getDataTermino()
        );
    }

    private ParticipacaoDetalheResponse toParticipacaoDetalhe(ParticipacaoMissao p) {
        return new ParticipacaoDetalheResponse(
                p.getAventureiro().getId(),
                p.getAventureiro().getNome(),
                p.getPapel(),
                p.getRecompensaOuro(),
                p.getDestaque()
        );
    }
}