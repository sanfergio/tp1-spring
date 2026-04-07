package com.guilda.registro.aventura.controller;

import com.guilda.registro.aventura.dto.AventureiroCreateRequest;
import com.guilda.registro.aventura.dto.CompanheiroRequest;
import com.guilda.registro.aventura.controller.AventureiroResponse;
import com.guilda.registro.aventura.service.AventureiroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aventura/aventureiros")
@RequiredArgsConstructor
public class AventureiroController {

    private final AventureiroService aventureiroService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AventureiroResponse criarAventureiro(@Valid @RequestBody AventureiroCreateRequest request) {
        return aventureiroService.criarAventureiro(request);
    }

    @PostMapping("/{id}/companheiro")
    public AventureiroResponse adicionarCompanheiro(@PathVariable Long id, @Valid @RequestBody CompanheiroRequest request) {
        return aventureiroService.adicionarCompanheiro(id, request);
    }

    @GetMapping("/{id}")
    public AventureiroResponse buscarAventureiro(@PathVariable Long id) {
        return aventureiroService.buscarPorId(id);
    }

    @GetMapping
    public List<AventureiroResponse> listarPorOrganizacao(@RequestParam Long orgId) {
        return aventureiroService.listarPorOrganizacao(orgId);
    }
}