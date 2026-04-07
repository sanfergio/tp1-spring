package com.guilda.registro.aventura.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;

@Entity
@Table(schema = "aventura", name = "participacoes_missao",
        uniqueConstraints = @UniqueConstraint(columnNames = {"missao_id", "aventureiro_id"}))
@Getter @Setter
public class ParticipacaoMissao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "missao_id", nullable = false)
    private Missao missao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aventureiro_id", nullable = false)
    private Aventureiro aventureiro;

    @Enumerated(EnumType.STRING)
    @Column(name = "papel", nullable = false)
    private PapelMissaoEnum papel;

    @Column(name = "recompensa_ouro")
    private Integer recompensaOuro;

    @Column(nullable = false)
    private Boolean destaque = false;

    @Column(name = "registered_at")
    private OffsetDateTime registeredAt;

    @PrePersist
    protected void onCreate() {
        registeredAt = OffsetDateTime.now();
    }
}