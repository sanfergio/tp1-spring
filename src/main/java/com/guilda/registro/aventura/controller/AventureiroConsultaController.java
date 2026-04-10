package com.guilda.registro.aventura.controller;

import com.guilda.registro.aventura.model.ClasseEnum;
import com.guilda.registro.aventura.service.AventureiroConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/aventureiros-consulta")
@RequiredArgsConstructor
public class AventureiroConsultaController {

    private final AventureiroConsultaService service;

    @GetMapping
    public Page<AventureiroResumoResponse> listarComFiltros(
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false) ClasseEnum classe,
            @RequestParam(required = false) Integer nivelMinimo,
            @PageableDefault(sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        return service.listarComFiltros(ativo, classe, nivelMinimo, pageable);
    }

    @GetMapping("/busca")
    public Page<AventureiroResumoResponse> buscarPorNome(
            @RequestParam String nome,
            @PageableDefault(sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        return service.buscarPorNome(nome, pageable);
    }

    @GetMapping("/{id}/perfil")
    public AventureiroDetalhadoResponse perfilCompleto(@PathVariable Long id) {
        return service.obterPerfilCompleto(id);
    }
}