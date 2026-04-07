package com.guilda.registro.aventura.model;

import com.guilda.registro.audit.model.Organizacao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "aventura", name = "missoes")
@Getter @Setter
public class Missao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacao_id", nullable = false)
    private Organizacao organizacao;

    @Column(length = 150, nullable = false)
    private String titulo;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_perigo", nullable = false)
    private NivelPerigoEnum nivelPerigo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusMissaoEnum status = StatusMissaoEnum.PLANEJADA;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_termino")
    private LocalDate dataTermino;

    @OneToMany(mappedBy = "missao", cascade = CascadeType.ALL)
    private List<ParticipacaoMissao> participacoes = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
    }
}