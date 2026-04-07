package com.guilda.registro.aventura.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "aventura", name = "companheiros")
@Getter @Setter
public class Companheiro {

    @Id
    private Long id; // mesmo id do aventureiro (relacionamento 1-1)

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "aventureiro_id")
    private Aventureiro aventureiro;

    @Column(length = 120, nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EspecieEnum especie;

    @Column(nullable = false)
    private Integer lealdade; // 0 a 100
}