package com.guilda.registro.operacoes.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;
import java.time.LocalDateTime;

@Entity
@Immutable
@Table(name = "mv_painel_tatico_missao", schema = "operacoes")
public class MissaoPainelTatico {

    @Id
    @Column(name = "missao_id")
    private Long missaoId;

    private String titulo;
    private String status;
    private String nivelPerigo;
    private Long organizacaoId;
    private Integer totalParticipantes;
    private Double nivelMedioEquipe;
    private Double totalRecompensa;
    private Integer totalMvps;
    private Integer participantesComCompanheiro;
    private LocalDateTime ultimaAtualizacao;
    private Double indiceProntidao;

    // getters e setters (obrigatórios para o Jackson e JPA)
    public Long getMissaoId() { return missaoId; }
    public void setMissaoId(Long missaoId) { this.missaoId = missaoId; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNivelPerigo() { return nivelPerigo; }
    public void setNivelPerigo(String nivelPerigo) { this.nivelPerigo = nivelPerigo; }
    public Long getOrganizacaoId() { return organizacaoId; }
    public void setOrganizacaoId(Long organizacaoId) { this.organizacaoId = organizacaoId; }
    public Integer getTotalParticipantes() { return totalParticipantes; }
    public void setTotalParticipantes(Integer totalParticipantes) { this.totalParticipantes = totalParticipantes; }
    public Double getNivelMedioEquipe() { return nivelMedioEquipe; }
    public void setNivelMedioEquipe(Double nivelMedioEquipe) { this.nivelMedioEquipe = nivelMedioEquipe; }
    public Double getTotalRecompensa() { return totalRecompensa; }
    public void setTotalRecompensa(Double totalRecompensa) { this.totalRecompensa = totalRecompensa; }
    public Integer getTotalMvps() { return totalMvps; }
    public void setTotalMvps(Integer totalMvps) { this.totalMvps = totalMvps; }
    public Integer getParticipantesComCompanheiro() { return participantesComCompanheiro; }
    public void setParticipantesComCompanheiro(Integer participantesComCompanheiro) { this.participantesComCompanheiro = participantesComCompanheiro; }
    public LocalDateTime getUltimaAtualizacao() { return ultimaAtualizacao; }
    public void setUltimaAtualizacao(LocalDateTime ultimaAtualizacao) { this.ultimaAtualizacao = ultimaAtualizacao; }
    public Double getIndiceProntidao() { return indiceProntidao; }
    public void setIndiceProntidao(Double indiceProntidao) { this.indiceProntidao = indiceProntidao; }
}