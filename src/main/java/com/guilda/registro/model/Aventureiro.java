package com.guilda.registro.model;

import jakarta.validation.constraints.*;

public class Aventureiro {

    private Long id;

    @NotBlank(message = "Nome do aventureiro é obrigatório")
    private String nome;

    @NotNull(message = "Classe é obrigatória")
    private ClasseEnum classe;

    @Min(value = 1, message = "Nível deve ser maior ou igual a 1")
    private int nivel;

    private boolean ativo;

    private Companheiro companheiro; // Opcional

    // Construtores
    public Aventureiro() {
        this.ativo = true; // inicia ativo
    }

    public Aventureiro(Long id, String nome, ClasseEnum classe, int nivel) {
        this.id = id;
        this.nome = nome;
        this.classe = classe;
        this.nivel = nivel;
        this.ativo = true;
        this.companheiro = null;
    }

    // Getters e setters
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
    public Companheiro getCompanheiro() { return companheiro; }
    public void setCompanheiro(Companheiro companheiro) { this.companheiro = companheiro; }
}