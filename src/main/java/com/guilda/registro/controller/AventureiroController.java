package com.guilda.registro.controller;

import com.guilda.registro.dto.request.AventureiroCreateRequest;
import com.guilda.registro.dto.request.AventureiroUpdateRequest;
import com.guilda.registro.dto.request.CompanheiroRequest;
import com.guilda.registro.dto.response.AventureiroDetalhadoResponse;
import com.guilda.registro.dto.response.AventureiroResumoResponse;
import com.guilda.registro.model.ClasseEnum;
import com.guilda.registro.service.AventureiroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aventureiros")
public class AventureiroController {

    @Autowired
    private AventureiroService service;

    // 1) Registrar aventureiro
    @PostMapping
    public ResponseEntity<AventureiroResumoResponse> registrar(@Valid @RequestBody AventureiroCreateRequest request) {
        AventureiroResumoResponse response = service.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 2) Listar com filtros e paginação
    @GetMapping
    public ResponseEntity<List<AventureiroResumoResponse>> listar(
            @RequestParam(required = false) ClasseEnum classe,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false) Integer nivelMinimo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // Validações de paginação
        if (page < 0) {
            throw new IllegalArgumentException("page não pode ser negativo");
        }
        if (size < 1 || size > 50) {
            throw new IllegalArgumentException("size deve estar entre 1 e 50");
        }

        Page<AventureiroResumoResponse> pagina = service.listar(classe, ativo, nivelMinimo, page, size);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(pagina.getTotalElements()));
        headers.add("X-Page", String.valueOf(pagina.getNumber()));
        headers.add("X-Size", String.valueOf(pagina.getSize()));
        headers.add("X-Total-Pages", String.valueOf(pagina.getTotalPages()));

        return ResponseEntity.ok().headers(headers).body(pagina.getContent());
    }

    // 3) Consultar por id
    @GetMapping("/{id}")
    public ResponseEntity<AventureiroDetalhadoResponse> buscarPorId(@PathVariable Long id) {
        AventureiroDetalhadoResponse response = service.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    // 4) Atualizar dados
    @PutMapping("/{id}")
    public ResponseEntity<AventureiroResumoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AventureiroUpdateRequest request) {
        AventureiroResumoResponse response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    // 5) Encerrar vínculo
    @PatchMapping("/{id}/encerrar-vinculo")
    public ResponseEntity<Void> encerrarVinculo(@PathVariable Long id) {
        service.encerrarVinculo(id);
        return ResponseEntity.noContent().build();
    }

    // 6) Recrutar novamente
    @PatchMapping("/{id}/recrutar")
    public ResponseEntity<Void> recrutar(@PathVariable Long id) {
        service.recrutar(id);
        return ResponseEntity.noContent().build();
    }

    // 7) Definir/substituir companheiro
    @PostMapping("/{id}/companheiro")
    public ResponseEntity<AventureiroDetalhadoResponse> definirCompanheiro(
            @PathVariable Long id,
            @Valid @RequestBody CompanheiroRequest request) {
        AventureiroDetalhadoResponse response = service.definirCompanheiro(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 8) Remover companheiro
    @DeleteMapping("/{id}/companheiro")
    public ResponseEntity<Void> removerCompanheiro(@PathVariable Long id) {
        service.removerCompanheiro(id);
        return ResponseEntity.noContent().build();
    }
}