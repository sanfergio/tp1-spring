package com.guilda.registro.aventura.controller;

import com.guilda.registro.aventura.model.NivelPerigoEnum;
import com.guilda.registro.aventura.model.StatusMissaoEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class MissaoResumoResponse {
    private Long id;
    private String titulo;
    private StatusMissaoEnum status;
    private NivelPerigoEnum nivelPerigo;
    private LocalDate dataInicio;
    private LocalDate dataTermino;
}