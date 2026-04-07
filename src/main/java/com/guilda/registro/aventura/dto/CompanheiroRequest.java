package com.guilda.registro.aventura.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CompanheiroRequest {
    @NotBlank @Size(max = 120)
    private String nome;

    @NotBlank
    private String especie;

    @Min(0) @Max(100)
    private Integer lealdade;
}