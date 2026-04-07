package com.guilda.registro.aventura.controller;

import com.guilda.registro.aventura.model.PapelMissaoEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParticipacaoDetalheResponse {
    private Long aventureiroId;
    private String aventureiroNome;
    private PapelMissaoEnum papel;
    private Integer recompensaOuro;
    private Boolean destaque;
}