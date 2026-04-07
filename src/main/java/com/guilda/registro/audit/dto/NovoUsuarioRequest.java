package com.guilda.registro.audit.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class NovoUsuarioRequest {
    @NotNull
    private Long organizacaoId;

    @NotBlank
    private String nome;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String senhaHash;

    private String status = "ATIVO";

    private Set<Long> roleIds; // opcional: já atribuir roles
}