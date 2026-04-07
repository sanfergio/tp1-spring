package com.guilda.registro.aventura.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class MissaoCreateRequest {
    @NotNull
    private Long organizacaoId;

    @NotBlank @Size(max = 150)
    private String titulo;

    @NotNull
    private String nivelPerigo;

    private LocalDate dataInicio;
    private LocalDate dataTermino;
}