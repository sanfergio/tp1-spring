package com.guilda.registro.aventura.controller;

import com.guilda.registro.aventura.model.ClasseEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class AventureiroDetalhadoResponse {
    private Long id;
    private String nome;
    private ClasseEnum classe;
    private Integer nivel;
    private Boolean ativo;
    private Long organizacaoId;
    private Long usuarioCadastroId;
    private CompanheiroResponse companheiro;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}