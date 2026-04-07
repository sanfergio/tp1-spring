package com.guilda.registro.aventura.controller;

import com.guilda.registro.aventura.model.ClasseEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AventureiroResumoResponse {
    private Long id;
    private String nome;
    private ClasseEnum classe;
    private Integer nivel;
    private Boolean ativo;
}