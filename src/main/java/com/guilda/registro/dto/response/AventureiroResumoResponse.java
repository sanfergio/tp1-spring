package com.guilda.registro.dto.response;

import com.guilda.registro.model.ClasseEnum;

public class AventureiroResumoResponse {
    private Long id;
    private String nome;
    private ClasseEnum classe;
    private int nivel;
    private boolean ativo;

    public AventureiroResumoResponse(Long id, String nome, ClasseEnum classe, int nivel, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.classe = classe;
        this.nivel = nivel;
        this.ativo = ativo;
    }

    // getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public ClasseEnum getClasse() { return classe; }
    public void setClasse(ClasseEnum classe) { this.classe = classe; }
    public int getNivel() { return nivel; }
    public void setNivel(int nivel) { this.nivel = nivel; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}