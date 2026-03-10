package com.guilda.registro.service;

import com.guilda.registro.dto.request.AventureiroCreateRequest;
import com.guilda.registro.dto.request.AventureiroUpdateRequest;
import com.guilda.registro.dto.request.CompanheiroRequest;
import com.guilda.registro.dto.response.*;
import com.guilda.registro.exception.RecursoNaoEncontradoException;
import com.guilda.registro.exception.ValidacaoException;
import com.guilda.registro.model.Aventureiro;
import com.guilda.registro.model.ClasseEnum;
import com.guilda.registro.model.Companheiro;
import com.guilda.registro.repository.AventureiroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AventureiroService {

    @Autowired
    private AventureiroRepository repository;

    // 1) Registrar aventureiro (sem companheiro)
    public AventureiroResumoResponse registrar(AventureiroCreateRequest request) {
        // Validações já feitas pelo @Valid no controller
        Aventureiro aventureiro = new Aventureiro();
        aventureiro.setNome(request.getNome());
        aventureiro.setClasse(request.getClasse());
        aventureiro.setNivel(request.getNivel());
        // ativo = true por padrão no construtor
        Aventureiro salvo = repository.salvar(aventureiro);
        return toResumoResponse(salvo);
    }

    // 2) Listar com filtros e paginação
    public Page<AventureiroResumoResponse> listar(ClasseEnum classe, Boolean ativo, Integer nivelMinimo, int page, int size) {
        // Validações de page e size já feitas no controller
        List<Aventureiro> todos = repository.listarTodos();

        // Aplicar filtros
        List<Aventureiro> filtrados = todos.stream()
                .filter(a -> classe == null || a.getClasse() == classe)
                .filter(a -> ativo == null || a.isAtivo() == ativo)
                .filter(a -> nivelMinimo == null || a.getNivel() >= nivelMinimo)
                .sorted(Comparator.comparing(Aventureiro::getId)) // ordem crescente de id
                .collect(Collectors.toList());

        // Paginação
        int start = page * size;
        int end = Math.min(start + size, filtrados.size());
        List<Aventureiro> pagina;
        if (start >= filtrados.size()) {
            pagina = List.of(); // página além do fim
        } else {
            pagina = filtrados.subList(start, end);
        }

        List<AventureiroResumoResponse> conteudo = pagina.stream()
                .map(this::toResumoResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(conteudo, PageRequest.of(page, size), filtrados.size());
    }

    // 3) Consultar por id
    public AventureiroDetalhadoResponse buscarPorId(Long id) {
        Aventureiro a = repository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aventureiro com id " + id + " não encontrado"));
        return toDetalhadoResponse(a);
    }

    // 4) Atualizar dados (nome, classe, nivel)
    public AventureiroResumoResponse atualizar(Long id, AventureiroUpdateRequest request) {
        Aventureiro a = repository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aventureiro com id " + id + " não encontrado"));

        a.setNome(request.getNome());
        a.setClasse(request.getClasse());
        a.setNivel(request.getNivel());

        repository.salvar(a);
        return toResumoResponse(a);
    }

    // 5) Encerrar vínculo (ativo = false)
    public void encerrarVinculo(Long id) {
        Aventureiro a = repository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aventureiro com id " + id + " não encontrado"));
        a.setAtivo(false);
        repository.salvar(a);
    }

    // 6) Recrutar novamente (ativo = true)
    public void recrutar(Long id) {
        Aventureiro a = repository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aventureiro com id " + id + " não encontrado"));
        a.setAtivo(true);
        repository.salvar(a);
    }

    // 7) Definir/substituir companheiro
    public AventureiroDetalhadoResponse definirCompanheiro(Long id, CompanheiroRequest request) {
        Aventureiro a = repository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aventureiro com id " + id + " não encontrado"));

        // Validações já aplicadas pelo @Valid
        Companheiro companheiro = new Companheiro(request.getNome(), request.getEspecie(), request.getLealdade());
        a.setCompanheiro(companheiro);

        repository.salvar(a);
        return toDetalhadoResponse(a);
    }

    // 8) Remover companheiro
    public void removerCompanheiro(Long id) {
        Aventureiro a = repository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aventureiro com id " + id + " não encontrado"));
        a.setCompanheiro(null);
        repository.salvar(a);
    }

    // Métodos auxiliares de conversão
    private AventureiroResumoResponse toResumoResponse(Aventureiro a) {
        return new AventureiroResumoResponse(a.getId(), a.getNome(), a.getClasse(), a.getNivel(), a.isAtivo());
    }

    private AventureiroDetalhadoResponse toDetalhadoResponse(Aventureiro a) {
        CompanheiroResponse compResp = null;
        if (a.getCompanheiro() != null) {
            compResp = new CompanheiroResponse(
                    a.getCompanheiro().getNome(),
                    a.getCompanheiro().getEspecie(),
                    a.getCompanheiro().getLealdade()
            );
        }
        return new AventureiroDetalhadoResponse(
                a.getId(), a.getNome(), a.getClasse(), a.getNivel(), a.isAtivo(), compResp
        );
    }
}