package com.guilda.registro.aventura.dto;

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

    @NotNull
    private String classe; // GUERREIRO, MAGO, ...

    @Min(1)
    private Integer nivel = 1;
}