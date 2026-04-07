package com.guilda.registro.aventura.controller;


import java.util.List;
import com.guilda.registro.aventura.dto.MissaoCreateRequest;
import com.guilda.registro.aventura.dto.ParticipacaoRequest;
import com.guilda.registro.aventura.controller.MissaoResponse;
import com.guilda.registro.aventura.controller.ParticipacaoResponse;
import com.guilda.registro.aventura.service.MissaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aventura/missoes")
@RequiredArgsConstructor
public class MissaoController {

    private final MissaoService missaoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MissaoResponse criarMissao(@Valid @RequestBody MissaoCreateRequest request) {
        return missaoService.criarMissao(request);
    }

    @PostMapping("/participacoes")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipacaoResponse inscreverAventureiro(@Valid @RequestBody ParticipacaoRequest request) {
        return missaoService.inscreverAventureiro(request);
    }

    @GetMapping
    public List<MissaoResponse> listarPorOrganizacao(@RequestParam Long orgId) {
        return missaoService.listarPorOrganizacao(orgId);
    }
}