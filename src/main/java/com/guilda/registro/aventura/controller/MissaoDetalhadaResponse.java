package com.guilda.registro.aventura.controller;

import com.guilda.registro.aventura.model.NivelPerigoEnum;
import com.guilda.registro.aventura.model.StatusMissaoEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class MissaoDetalhadaResponse {
    private Long id;
    private String titulo;
    private NivelPerigoEnum nivelPerigo;
    private StatusMissaoEnum status;
    private LocalDate dataInicio;
    private LocalDate dataTermino;
    private Long organizacaoId;
    private OffsetDateTime createdAt;
    private List<ParticipacaoDetalheResponse> participantes;
}