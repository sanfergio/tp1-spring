package com.guilda.registro.aventura.controller;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RankingResponse {
    private Long aventureiroId;
    private String aventureiroNome;
    private Long totalParticipacoes;
    private Long somaRecompensas;
    private Long totalDestaques;
}