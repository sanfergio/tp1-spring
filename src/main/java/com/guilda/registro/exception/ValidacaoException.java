package com.guilda.registro.exception;

import java.util.List;

public class ValidacaoException extends RuntimeException {
    private final List<String> detalhes;

    public ValidacaoException(String mensagem, List<String> detalhes) {
        super(mensagem);
        this.detalhes = detalhes;
    }

    public List<String> getDetalhes() {
        return detalhes;
    }
}