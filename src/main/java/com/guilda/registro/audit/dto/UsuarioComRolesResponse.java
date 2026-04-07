package com.guilda.registro.audit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Set;

@Data
@AllArgsConstructor
public class UsuarioComRolesResponse {
    private Long id;
    private String nome;
    private String email;
    private String status;
    private String organizacaoNome;
    private Set<String> roles;
}