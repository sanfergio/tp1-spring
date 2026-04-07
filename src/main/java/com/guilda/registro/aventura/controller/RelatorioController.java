package com.guilda.registro.aventura.controller;

import com.guilda.registro.aventura.controller.MissaoMetricasResponse;
import com.guilda.registro.aventura.controller.RankingResponse;
import com.guilda.registro.aventura.model.StatusMissaoEnum;
import com.guilda.registro.aventura.service.RelatorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioService service;

    @GetMapping("/ranking")
    public List<RankingResponse> rankingParticipacao(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(required = false) StatusMissaoEnum statusMissao) {
        return service.rankingParticipacao(dataInicio, dataFim, statusMissao);
    }

    @GetMapping("/missoes-metricas")
    public List<MissaoMetricasResponse> relatorioMissoesMetricas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        return service.relatorioMissoesMetricas(dataInicio, dataFim);
    }
}