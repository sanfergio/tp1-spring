package com.guilda.registro.dto.request;

import com.guilda.registro.model.ClasseEnum;
import jakarta.validation.constraints.*;

public class AventureiroCreateRequest {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "Classe é obrigatória")
    private ClasseEnum classe;

    @Min(value = 1, message = "Nível deve ser >= 1")
    private int nivel;

    // getters e setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public ClasseEnum getClasse() { return classe; }
    public void setClasse(ClasseEnum classe) { this.classe = classe; }
    public int getNivel() { return nivel; }
    public void setNivel(int nivel) { this.nivel = nivel; }
}