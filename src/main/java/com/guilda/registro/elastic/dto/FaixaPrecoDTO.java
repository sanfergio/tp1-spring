package com.guilda.registro.elastic.dto;

public class FaixaPrecoDTO {
    private String faixa;
    private long quantidade;

    public FaixaPrecoDTO(String faixa, long quantidade) {
        this.faixa = faixa;
        this.quantidade = quantidade;
    }

    public String getFaixa() { return faixa; }
    public long getQuantidade() { return quantidade; }
}