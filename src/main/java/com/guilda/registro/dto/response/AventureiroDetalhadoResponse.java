package com.guilda.registro.dto.response;

import com.guilda.registro.model.ClasseEnum;

public class AventureiroDetalhadoResponse {
    private Long id;
    private String nome;
    private ClasseEnum classe;
    private int nivel;
    private boolean ativo;
    private CompanheiroResponse companheiro;

    public AventureiroDetalhadoResponse(Long id, String nome, ClasseEnum classe, int nivel, boolean ativo, CompanheiroResponse companheiro) {
        this.id = id;
        this.nome = nome;
        this.classe = classe;
        this.nivel = nivel;
        this.ativo = ativo;
        this.companheiro = companheiro;
    }

    // getters e setters
    // ...
}