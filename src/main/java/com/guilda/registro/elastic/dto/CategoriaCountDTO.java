package com.guilda.registro.elastic.dto;

public class CategoriaCountDTO {
    private String categoria;
    private long quantidade;

    public CategoriaCountDTO(String categoria, long quantidade) {
        this.categoria = categoria;
        this.quantidade = quantidade;
    }

    public String getCategoria() { return categoria; }
    public long getQuantidade() { return quantidade; }
}