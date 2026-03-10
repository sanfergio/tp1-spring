package com.guilda.registro.dto.request;

import com.guilda.registro.model.EspecieEnum;
import jakarta.validation.constraints.*;

public class CompanheiroRequest {

    @NotBlank(message = "Nome do companheiro é obrigatório")
    private String nome;

    @NotNull(message = "Espécie é obrigatória")
    private EspecieEnum especie;

    @Min(value = 0, message = "Lealdade mínima é 0")
    @Max(value = 100, message = "Lealdade máxima é 100")
    private int lealdade;

    // getters e setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public EspecieEnum getEspecie() { return especie; }
    public void setEspecie(EspecieEnum especie) { this.especie = especie; }
    public int getLealdade() { return lealdade; }
    public void setLealdade(int lealdade) { this.lealdade = lealdade; }
}