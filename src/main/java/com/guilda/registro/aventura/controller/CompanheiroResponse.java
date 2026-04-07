package com.guilda.registro.aventura.controller;

import com.guilda.registro.aventura.model.EspecieEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompanheiroResponse {
    private String nome;
    private EspecieEnum especie;
    private Integer lealdade;
}