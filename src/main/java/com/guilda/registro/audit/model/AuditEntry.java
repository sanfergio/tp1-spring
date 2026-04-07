package com.guilda.registro.audit.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.net.InetAddress;
import java.time.OffsetDateTime;

@Entity
@Table(name = "audit_entries", schema = "audit")
@Getter @Setter
public class AuditEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacao_id", nullable = false)
    private Organizacao organizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_user_id")
    private Usuario actorUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_api_key_id")
    private ApiKey actorApiKey;

    private String action;

    @Column(name = "entity_schema")
    private String entitySchema;

    @Column(name = "entity_name")
    private String entityName;

    @Column(name = "entity_id")
    private String entityId;

    @Column(name = "occurred_at")
    private OffsetDateTime occurredAt;

    private InetAddress ip;

    @Column(name = "user_agent")
    private String userAgent;

    @JdbcTypeCode(SqlTypes.JSON)
    private String diff;

    @JdbcTypeCode(SqlTypes.JSON)
    private String metadata;

    private Boolean success;
}