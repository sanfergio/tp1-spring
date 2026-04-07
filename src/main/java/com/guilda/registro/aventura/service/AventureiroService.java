package com.guilda.registro.aventura.service;

import com.guilda.registro.aventura.dto.AventureiroCreateRequest;
import com.guilda.registro.aventura.dto.CompanheiroRequest;
import com.guilda.registro.aventura.controller.AventureiroResponse;
import com.guilda.registro.aventura.controller.CompanheiroResponse;
import com.guilda.registro.aventura.model.Aventureiro;
import com.guilda.registro.aventura.model.ClasseEnum;
import com.guilda.registro.aventura.model.Companheiro;
import com.guilda.registro.aventura.model.EspecieEnum;
import com.guilda.registro.aventura.repository.AventureiroRepository;
import com.guilda.registro.aventura.repository.CompanheiroRepository;
import com.guilda.registro.audit.model.Organizacao;
import com.guilda.registro.audit.model.Usuario;
import com.guilda.registro.audit.repository.OrganizacaoRepository;
import com.guilda.registro.audit.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AventureiroService {

    private final AventureiroRepository aventureiroRepository;
    private final CompanheiroRepository companheiroRepository;
    private final OrganizacaoRepository organizacaoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public AventureiroResponse criarAventureiro(AventureiroCreateRequest request) {
        Organizacao org = organizacaoRepository.findById(request.getOrganizacaoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Organização não encontrada"));
        Usuario user = usuarioRepository.findById(request.getUsuarioCadastroId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        ClasseEnum classe;
        try {
            classe = ClasseEnum.valueOf(request.getClasse().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Classe inválida");
        }

        Aventureiro aventureiro = new Aventureiro();
        aventureiro.setOrganizacao(org);
        aventureiro.setUsuarioCadastro(user);
        aventureiro.setNome(request.getNome());
        aventureiro.setClasse(classe);
        aventureiro.setNivel(request.getNivel() != null ? request.getNivel() : 1);
        aventureiro.setAtivo(true);
        aventureiro.setCreatedAt(OffsetDateTime.now());
        aventureiro.setUpdatedAt(OffsetDateTime.now());

        Aventureiro saved = aventureiroRepository.save(aventureiro);
        return toResponse(saved);
    }

    @Transactional
    public AventureiroResponse adicionarCompanheiro(Long id, CompanheiroRequest request) {
        Aventureiro aventureiro = aventureiroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aventureiro não encontrado"));

        EspecieEnum especie;
        try {
            especie = EspecieEnum.valueOf(request.getEspecie().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Espécie inválida");
        }
        if (request.getLealdade() < 0 || request.getLealdade() > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lealdade deve estar entre 0 e 100");
        }

        Companheiro companheiro = aventureiro.getCompanheiro();
        if (companheiro == null) {
            companheiro = new Companheiro();
            companheiro.setAventureiro(aventureiro);
        }
        companheiro.setNome(request.getNome());
        companheiro.setEspecie(especie);
        companheiro.setLealdade(request.getLealdade());

        companheiroRepository.save(companheiro);
        aventureiro.setCompanheiro(companheiro);
        Aventureiro updated = aventureiroRepository.save(aventureiro);
        return toResponse(updated);
    }

    public AventureiroResponse buscarPorId(Long id) {
        Aventureiro aventureiro = aventureiroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aventureiro não encontrado"));
        return toResponse(aventureiro);
    }

    public List<AventureiroResponse> listarPorOrganizacao(Long orgId) {
        if (!organizacaoRepository.existsById(orgId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Organização não encontrada");
        }
        List<Aventureiro> aventureiros = aventureiroRepository.findByOrganizacaoId(orgId);
        return aventureiros.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private AventureiroResponse toResponse(Aventureiro a) {
        CompanheiroResponse comp = null;
        if (a.getCompanheiro() != null) {
            comp = new CompanheiroResponse(
                    a.getCompanheiro().getNome(),
                    a.getCompanheiro().getEspecie(),
                    a.getCompanheiro().getLealdade()
            );
        }
        return new AventureiroResponse(
                a.getId(),
                a.getNome(),
                a.getClasse(),
                a.getNivel(),
                a.getAtivo(),
                a.getOrganizacao().getId(),
                a.getUsuarioCadastro().getId(),
                comp,
                a.getCreatedAt(),
                a.getUpdatedAt()
        );
    }
}