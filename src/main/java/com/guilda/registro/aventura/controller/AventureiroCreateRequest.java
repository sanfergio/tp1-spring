package com.guilda.registro.aventura.controller;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AventureiroCreateRequest {
    @NotNull
    private Long organizacaoId;

    @NotNull
    private Long usuarioCadastroId;

    @NotBlank @Size(max = 120)
    private String nome;

    @NotBlank
    private String classe;

    @Min(1)
    private Integer nivel = 1;
}