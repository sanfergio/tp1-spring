package com.guilda.registro.dto.response;

import java.util.List;

public class ErroResponse {
    private String mensagem;
    private List<String> detalhes;

    public ErroResponse(String mensagem, List<String> detalhes) {
        this.mensagem = mensagem;
        this.detalhes = detalhes;
    }

    // getters e setters
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
    public List<String> getDetalhes() { return detalhes; }
    public void setDetalhes(List<String> detalhes) { this.detalhes = detalhes; }
}