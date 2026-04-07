package com.guilda.registro.audit.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;

@Entity
@Table(name = "api_keys", schema = "audit")
@Getter @Setter
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacao_id", nullable = false)
    private Organizacao organizacao;

    private String nome;

    @Column(name = "key_hash")
    private String keyHash;

    private Boolean ativo;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "last_used_at")
    private OffsetDateTime lastUsedAt;
}