package com.guilda.registro.aventura.controller;

import com.guilda.registro.aventura.model.NivelPerigoEnum;
import com.guilda.registro.aventura.model.StatusMissaoEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MissaoMetricasResponse {
    private Long missaoId;
    private String titulo;
    private StatusMissaoEnum status;
    private NivelPerigoEnum nivelPerigo;
    private Long quantidadeParticipantes;
    private Long totalRecompensasDistribuidas;
}