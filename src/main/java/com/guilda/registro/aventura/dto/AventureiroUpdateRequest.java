package com.guilda.registro.aventura.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AventureiroUpdateRequest {
    @Size(max = 120)
    private String nome;
    private String classe;
    @Min(1)
    private Integer nivel;
}