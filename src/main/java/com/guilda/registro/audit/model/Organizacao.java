package com.guilda.registro.audit.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "organizacoes", schema = "audit")
@Getter @Setter
public class Organizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nome;

    private Boolean ativo;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "organizacao")
    private List<Usuario> usuarios = new ArrayList<>();

    @OneToMany(mappedBy = "organizacao")
    private List<ApiKey> apiKeys = new ArrayList<>();

    @OneToMany(mappedBy = "organizacao")
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "organizacao")
    private List<AuditEntry> auditEntries = new ArrayList<>();
}