package com.guilda.registro.aventura.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ParticipacaoRequest {
    @NotNull
    private Long missaoId;

    @NotNull
    private Long aventureiroId;

    @NotNull
    private String papel;

    private Integer recompensaOuro;
    private Boolean destaque = false;
}