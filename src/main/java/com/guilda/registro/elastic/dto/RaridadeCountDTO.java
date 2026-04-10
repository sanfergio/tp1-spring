package com.guilda.registro.elastic.dto;

public class RaridadeCountDTO {
    private String raridade;
    private long quantidade;

    public RaridadeCountDTO(String raridade, long quantidade) {
        this.raridade = raridade;
        this.quantidade = quantidade;
    }

    public String getRaridade() { return raridade; }
    public long getQuantidade() { return quantidade; }
}