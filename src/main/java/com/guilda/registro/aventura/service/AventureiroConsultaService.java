package com.guilda.registro.aventura.service;

import com.guilda.registro.aventura.controller.*;
import com.guilda.registro.aventura.model.Aventureiro;
import com.guilda.registro.aventura.model.ClasseEnum;
import com.guilda.registro.aventura.model.Missao;
import com.guilda.registro.aventura.model.ParticipacaoMissao;
import com.guilda.registro.aventura.repository.AventureiroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AventureiroConsultaService {

    private final AventureiroRepository aventureiroRepository;

    @Transactional(readOnly = true)
    public Page<AventureiroResumoResponse> listarComFiltros(Boolean ativo, ClasseEnum classe, Integer nivelMinimo, Pageable pageable) {
        Page<Aventureiro> page = aventureiroRepository.findWithFilters(ativo, classe, nivelMinimo, pageable);
        return page.map(this::toResumoResponse);
    }

    @Transactional(readOnly = true)
    public Page<AventureiroResumoResponse> buscarPorNome(String nome, Pageable pageable) {
        Page<Aventureiro> page = aventureiroRepository.searchByNome(nome, pageable);
        return page.map(this::toResumoResponse);
    }

    @Transactional(readOnly = true)
    public AventureiroDetalhadoResponse obterPerfilCompleto(Long id) {
        Aventureiro aventureiro = aventureiroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aventureiro não encontrado"));

        int totalParticipacoes = aventureiro.getParticipacoes().size();

        Optional<Missao> ultimaMissao = aventureiro.getParticipacoes().stream()
                .map(ParticipacaoMissao::getMissao)
                .max(Comparator.comparing(Missao::getCreatedAt));

        MissaoResumoResponse ultimaMissaoResponse = ultimaMissao.map(m -> new MissaoResumoResponse(
                m.getId(), m.getTitulo(), m.getStatus(), m.getNivelPerigo(),
                m.getDataInicio(), m.getDataTermino()
        )).orElse(null);

        CompanheiroResponse companheiroResponse = null;
        if (aventureiro.getCompanheiro() != null) {
            companheiroResponse = new CompanheiroResponse(
                    aventureiro.getCompanheiro().getNome(),
                    aventureiro.getCompanheiro().getEspecie(),
                    aventureiro.getCompanheiro().getLealdade()
            );
        }

        return new AventureiroDetalhadoResponse(
                aventureiro.getId(),
                aventureiro.getNome(),
                aventureiro.getClasse(),
                aventureiro.getNivel(),
                aventureiro.getAtivo(),
                aventureiro.getOrganizacao().getId(),
                aventureiro.getUsuarioCadastro().getId(),
                companheiroResponse,
                aventureiro.getCreatedAt(),
                aventureiro.getUpdatedAt(),
                totalParticipacoes,
                ultimaMissaoResponse
        );
    }

    private AventureiroResumoResponse toResumoResponse(Aventureiro a) {
        return new AventureiroResumoResponse(a.getId(), a.getNome(), a.getClasse(), a.getNivel(), a.getAtivo());
    }
}