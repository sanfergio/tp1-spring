package com.guilda.registro.operacoes.controller;

import com.guilda.registro.operacoes.model.MissaoPainelTatico;
import com.guilda.registro.operacoes.service.MissaoTaticaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/missoes")
public class MissaoTaticaController {

    private final MissaoTaticaService service;

    public MissaoTaticaController(MissaoTaticaService service) {
        this.service = service;
    }

    @GetMapping("/top15dias")
    public ResponseEntity<List<MissaoPainelTatico>> getTopMissoesUltimos15Dias() {
        List<MissaoPainelTatico> missoes = service.getTopMissoesUltimos15Dias();
        return ResponseEntity.ok(missoes);
    }
}