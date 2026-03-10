package com.guilda.registro.dto.response;

import com.guilda.registro.model.EspecieEnum;

public class CompanheiroResponse {
    private String nome;
    private EspecieEnum especie;
    private int lealdade;

    public CompanheiroResponse(String nome, EspecieEnum especie, int lealdade) {
        this.nome = nome;
        this.especie = especie;
        this.lealdade = lealdade;
    }

    // getters e setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public EspecieEnum getEspecie() { return especie; }
    public void setEspecie(EspecieEnum especie) { this.especie = especie; }
    public int getLealdade() { return lealdade; }
    public void setLealdade(int lealdade) { this.lealdade = lealdade; }
}