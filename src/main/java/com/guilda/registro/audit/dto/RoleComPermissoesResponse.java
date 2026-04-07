package com.guilda.registro.audit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Set;

@Data
@AllArgsConstructor
public class RoleComPermissoesResponse {
    private Long id;
    private String nome;
    private String descricao;
    private String organizacaoNome;
    private Set<String> permissoes; 
}