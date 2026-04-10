package com.guilda.registro.aventura.controller;


import java.util.List;
import com.guilda.registro.aventura.dto.MissaoCreateRequest;
import com.guilda.registro.aventura.dto.ParticipacaoRequest;
import com.guilda.registro.aventura.service.MissaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/top15dias")
    public List<MissaoResponse> obterTop10Ultimos15Dias() {
        return missaoService.obterTop10Ultimos15Dias();
    }

    @PatchMapping("/{id}/iniciar")
    @ResponseStatus(HttpStatus.OK)
    public MissaoResponse iniciarMissao(@PathVariable Long id) {
        return missaoService.iniciarMissao(id);
    }

    @PatchMapping("/{id}/concluir")
    @ResponseStatus(HttpStatus.OK)
    public MissaoResponse concluirMissao(@PathVariable Long id) {
        return missaoService.concluirMissao(id);
    }

    @PatchMapping("/{id}/cancelar")
    @ResponseStatus(HttpStatus.OK)
    public MissaoResponse cancelarMissao(@PathVariable Long id) {
        return missaoService.cancelarMissao(id);
    }
}