package com.guilda.registro.aventura.service;

import com.guilda.registro.aventura.dto.MissaoCreateRequest;
import com.guilda.registro.aventura.dto.ParticipacaoRequest;
import com.guilda.registro.aventura.controller.MissaoResponse;
import com.guilda.registro.aventura.controller.ParticipacaoResponse;
import com.guilda.registro.aventura.model.*;
import com.guilda.registro.aventura.repository.*;
import com.guilda.registro.audit.model.Organizacao;
import com.guilda.registro.audit.repository.OrganizacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;
import java.time.OffsetDateTime;
import org.springframework.cache.annotation.Cacheable;

@Service
@RequiredArgsConstructor
public class MissaoService {

    private final MissaoRepository missaoRepository;
    private final OrganizacaoRepository organizacaoRepository;
    private final AventureiroRepository aventureiroRepository;
    private final ParticipacaoMissaoRepository participacaoRepository;

    @Transactional
    @CacheEvict(value = {"topMissoes15Dias", "missoesUltimosDias", "missoesAltaProntidao", "missoesEmAndamento"}, allEntries = true)
    public MissaoResponse criarMissao(MissaoCreateRequest request) {
        Organizacao org = organizacaoRepository.findById(request.getOrganizacaoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Organização não encontrada"));

        Missao missao = new Missao();
        missao.setOrganizacao(org);
        missao.setTitulo(request.getTitulo());
        missao.setNivelPerigo(NivelPerigoEnum.valueOf(request.getNivelPerigo()));
        missao.setDataInicio(request.getDataInicio());
        missao.setDataTermino(request.getDataTermino());

        Missao saved = missaoRepository.save(missao);
        return toResponse(saved);
    }

    @Transactional
    @CacheEvict(value = {"topMissoes15Dias", "missoesUltimosDias", "missoesAltaProntidao", "missoesEmAndamento"}, allEntries = true)
    public ParticipacaoResponse inscreverAventureiro(ParticipacaoRequest request) {
        Missao missao = missaoRepository.findById(request.getMissaoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Missão não encontrada"));

        Aventureiro aventureiro = aventureiroRepository.findById(request.getAventureiroId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aventureiro não encontrado"));

        if (!aventureiro.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Aventureiro inativo não pode participar de missões");
        }

        if (missao.getStatus() != StatusMissaoEnum.PLANEJADA && missao.getStatus() != StatusMissaoEnum.EM_ANDAMENTO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missão não aceita novos participantes (status: " + missao.getStatus() + ")");
        }

        if (participacaoRepository.existsByMissaoIdAndAventureiroId(missao.getId(), aventureiro.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Aventureiro já está inscrito nesta missão");
        }

        if (!aventureiro.getOrganizacao().getId().equals(missao.getOrganizacao().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Aventureiro pertence a outra organização");
        }

        ParticipacaoMissao participacao = new ParticipacaoMissao();
        participacao.setMissao(missao);
        participacao.setAventureiro(aventureiro);
        participacao.setPapel(PapelMissaoEnum.valueOf(request.getPapel()));
        participacao.setRecompensaOuro(request.getRecompensaOuro());
        participacao.setDestaque(request.getDestaque() != null ? request.getDestaque() : false);

        ParticipacaoMissao saved = participacaoRepository.save(participacao);
        return toParticipacaoResponse(saved);
    }

    public List<MissaoResponse> listarPorOrganizacao(Long orgId) {
        return missaoRepository.findByOrganizacaoId(orgId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "topMissoes15Dias")
    public List<MissaoResponse> obterTop10Ultimos15Dias() {
        OffsetDateTime dataInicio = OffsetDateTime.now().minusDays(15);
        return missaoRepository.findTop10Top15Dias(dataInicio).stream()
                .limit(10)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(value = {"topMissoes15Dias", "missoesUltimosDias", "missoesAltaProntidao", "missoesEmAndamento"}, allEntries = true)
    public MissaoResponse iniciarMissao(Long missaoId) {
        Missao missao = missaoRepository.findById(missaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Missão não encontrada"));

        if (missao.getStatus() != StatusMissaoEnum.PLANEJADA) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Apenas missões em status PLANEJADA podem ser iniciadas. Status atual: " + missao.getStatus());
        }

        missao.setStatus(StatusMissaoEnum.EM_ANDAMENTO);
        Missao updated = missaoRepository.save(missao);
        return toResponse(updated);
    }

    @Transactional
    @CacheEvict(value = {"topMissoes15Dias", "missoesUltimosDias", "missoesAltaProntidao", "missoesEmAndamento"}, allEntries = true)
    public MissaoResponse concluirMissao(Long missaoId) {
        Missao missao = missaoRepository.findById(missaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Missão não encontrada"));

        if (missao.getStatus() != StatusMissaoEnum.EM_ANDAMENTO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Apenas missões em status EM_ANDAMENTO podem ser concluídas. Status atual: " + missao.getStatus());
        }

        missao.setStatus(StatusMissaoEnum.CONCLUIDA);
        Missao updated = missaoRepository.save(missao);
        return toResponse(updated);
    }

    @Transactional
    @CacheEvict(value = {"topMissoes15Dias", "missoesUltimosDias", "missoesAltaProntidao", "missoesEmAndamento"}, allEntries = true)
    public MissaoResponse cancelarMissao(Long missaoId) {
        Missao missao = missaoRepository.findById(missaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Missão não encontrada"));

        if (missao.getStatus() == StatusMissaoEnum.CONCLUIDA || missao.getStatus() == StatusMissaoEnum.CANCELADA) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Missões concluídas ou canceladas não podem ser alteradas. Status atual: " + missao.getStatus());
        }

        missao.setStatus(StatusMissaoEnum.CANCELADA);
        Missao updated = missaoRepository.save(missao);
        return toResponse(updated);
    }

    private MissaoResponse toResponse(Missao missao) {
        List<ParticipacaoResponse> participacoes = missao.getParticipacoes().stream()
                .map(this::toParticipacaoResponse)
                .collect(Collectors.toList());

        return new MissaoResponse(
                missao.getId(),
                missao.getTitulo(),
                missao.getNivelPerigo(),
                missao.getStatus(),
                missao.getDataInicio(),
                missao.getDataTermino(),
                missao.getOrganizacao().getId(),
                missao.getCreatedAt(),
                participacoes
        );
    }

    private ParticipacaoResponse toParticipacaoResponse(ParticipacaoMissao p) {
        return new ParticipacaoResponse(
                p.getId(),
                p.getMissao().getId(),
                p.getAventureiro().getId(),
                p.getPapel(),
                p.getRecompensaOuro(),
                p.getDestaque(),
                p.getRegisteredAt()
        );
    }
}