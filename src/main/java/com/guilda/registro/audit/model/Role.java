package com.guilda.registro.audit.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles", schema = "audit")
@Getter @Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacao_id", nullable = false)
    private Organizacao organizacao;

    private String nome;

    private String descricao;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    // Mapeamento N:N com Permission
    @ManyToMany
    @JoinTable(
            name = "role_permissions",
            schema = "audit",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();

    // Mapeamento inverso de Usuario (já mapeado em Usuario.roles)
    @ManyToMany(mappedBy = "roles")
    private Set<Usuario> usuarios = new HashSet<>();
}