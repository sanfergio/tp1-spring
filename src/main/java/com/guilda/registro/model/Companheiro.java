package com.guilda.registro.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Companheiro {

    @NotBlank(message = "Nome do companheiro é obrigatório")
    private String nome;

    @NotNull(message = "Espécie é obrigatória")
    private EspecieEnum especie;

    @Min(value = 0, message = "Lealdade deve ser no mínimo 0")
    @Max(value = 100, message = "Lealdade deve ser no máximo 100")
    private int lealdade;

    // Construtores, getters e setters
    public Companheiro() {}

    public Companheiro(String nome, EspecieEnum especie, int lealdade) {
        this.nome = nome;
        this.especie = especie;
        this.lealdade = lealdade;
    }

    // Getters e setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public EspecieEnum getEspecie() { return especie; }
    public void setEspecie(EspecieEnum especie) { this.especie = especie; }
    public int getLealdade() { return lealdade; }
    public void setLealdade(int lealdade) { this.lealdade = lealdade; }
}