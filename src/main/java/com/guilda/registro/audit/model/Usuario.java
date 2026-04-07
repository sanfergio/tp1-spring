package com.guilda.registro.audit.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios", schema = "audit")
@Getter @Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacao_id", nullable = false)
    private Organizacao organizacao;

    private String nome;

    @Column(name = "email")
    private String email;

    @Column(name = "senha_hash")
    private String senhaHash;

    private String status;

    @Column(name = "ultimo_login_em")
    private OffsetDateTime ultimoLoginEm;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            schema = "audit",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}