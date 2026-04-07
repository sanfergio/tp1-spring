package com.guilda.registro.aventura.controller;

import com.guilda.registro.aventura.model.PapelMissaoEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class ParticipacaoResponse {
    private Long id;
    private Long missaoId;
    private Long aventureiroId;
    private PapelMissaoEnum papel;
    private Integer recompensaOuro;
    private Boolean destaque;
    private OffsetDateTime registeredAt;
}