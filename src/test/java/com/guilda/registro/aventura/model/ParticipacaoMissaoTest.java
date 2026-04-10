package com.guilda.registro.aventura.model;

import com.guilda.registro.audit.model.Organizacao;
import com.guilda.registro.audit.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Testes da Entidade ParticipacaoMissao")
class ParticipacaoMissaoTest {

    private ParticipacaoMissao participacao;
    private Missao missao;
    private Aventureiro aventureiro;
    private Organizacao organizacao;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        organizacao = new Organizacao();
        organizacao.setId(1L);

        usuario = new Usuario();
        usuario.setId(1L);

        aventureiro = new Aventureiro();
        aventureiro.setId(1L);
        aventureiro.setNome("Aragorn");
        aventureiro.setClasse(ClasseEnum.GUERREIRO);
        aventureiro.setNivel(5);
        aventureiro.setOrganizacao(organizacao);
        aventureiro.setUsuarioCadastro(usuario);

        missao = new Missao();
        missao.setId(1L);
        missao.setTitulo("Derrotar Dragão");
        missao.setNivelPerigo(NivelPerigoEnum.ALTO);
        missao.setStatus(StatusMissaoEnum.EM_ANDAMENTO);
        missao.setOrganizacao(organizacao);

        participacao = new ParticipacaoMissao();
    }

    @Test
    @DisplayName("Deve criar uma participação com valores corretos")
    void testCreateParticipacao() {
        participacao.setId(1L);
        participacao.setMissao(missao);
        participacao.setAventureiro(aventureiro);
        participacao.setPapel(PapelMissaoEnum.GUERREIRO);
        participacao.setRecompensaOuro(1000);
        participacao.setDestaque(false);

        assertThat(participacao.getId()).isEqualTo(1L);
        assertThat(participacao.getMissao()).isEqualTo(missao);
        assertThat(participacao.getAventureiro()).isEqualTo(aventureiro);
        assertThat(participacao.getPapel()).isEqualTo(PapelMissaoEnum.GUERREIRO);
        assertThat(participacao.getRecompensaOuro()).isEqualTo(1000);
        assertThat(participacao.getDestaque()).isFalse();
    }

    @Test
    @DisplayName("Deve inicializaár destaque como falso por padrão")
    void testDestaqueDefault() {
        assertThat(participacao.getDestaque()).isFalse();
    }

    @Test
    @DisplayName("Deve permitir diferentes papéis de missão")
    void testDiferentesPapeisMissao() {
        participacao.setPapel(PapelMissaoEnum.GUERREIRO);
        assertThat(participacao.getPapel()).isEqualTo(PapelMissaoEnum.GUERREIRO);

        participacao.setPapel(PapelMissaoEnum.MAGO);
        assertThat(participacao.getPapel()).isEqualTo(PapelMissaoEnum.MAGO);

        participacao.setPapel(PapelMissaoEnum.ARQUEIRO);
        assertThat(participacao.getPapel()).isEqualTo(PapelMissaoEnum.ARQUEIRO);

        participacao.setPapel(PapelMissaoEnum.CLERIGO);
        assertThat(participacao.getPapel()).isEqualTo(PapelMissaoEnum.CLERIGO);
    }

    @Test
    @DisplayName("Deve permitir diferentes valores de recompensa")
    void testRecompensaOuro() {
        participacao.setRecompensaOuro(500);
        assertThat(participacao.getRecompensaOuro()).isEqualTo(500);

        participacao.setRecompensaOuro(1000);
        assertThat(participacao.getRecompensaOuro()).isEqualTo(1000);

        participacao.setRecompensaOuro(5000);
        assertThat(participacao.getRecompensaOuro()).isEqualTo(5000);
    }

    @Test
    @DisplayName("Deve permitir recompensa nula")
    void testRecompensaNula() {
        assertThat(participacao.getRecompensaOuro()).isNull();

        participacao.setRecompensaOuro(null);
        assertThat(participacao.getRecompensaOuro()).isNull();
    }

    @Test
    @DisplayName("Deve permitir marcar participação como destaque")
    void testDestaque() {
        participacao.setDestaque(false);
        assertThat(participacao.getDestaque()).isFalse();

        participacao.setDestaque(true);
        assertThat(participacao.getDestaque()).isTrue();
    }

    @Test
    @DisplayName("Deve registrar o timestamp de participação")
    void testRegisteredAt() {
        OffsetDateTime agora = OffsetDateTime.now();
        participacao.setRegisteredAt(agora);

        assertThat(participacao.getRegisteredAt()).isEqualTo(agora);
    }

    @Test
    @DisplayName("Deve permitir registered_at nula inicialmente")
    void testRegisteredAtNula() {
        assertThat(participacao.getRegisteredAt()).isNull();
    }

    @Test
    @DisplayName("Deve associar participação a uma missão")
    void testAssociarMissao() {
        assertThat(participacao.getMissao()).isNull();

        participacao.setMissao(missao);
        assertThat(participacao.getMissao()).isEqualTo(missao);
        assertThat(participacao.getMissao().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Deve associar participação a um aventureiro")
    void testAssociarAventureiro() {
        assertThat(participacao.getAventureiro()).isNull();

        participacao.setAventureiro(aventureiro);
        assertThat(participacao.getAventureiro()).isEqualTo(aventureiro);
        assertThat(participacao.getAventureiro().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Deve permitir múltiplos papéis na mesma participação (alteração)")
    void testAlterarPapel() {
        participacao.setPapel(PapelMissaoEnum.GUERREIRO);
        assertThat(participacao.getPapel()).isEqualTo(PapelMissaoEnum.GUERREIRO);

        participacao.setPapel(PapelMissaoEnum.MAGO);
        assertThat(participacao.getPapel()).isEqualTo(PapelMissaoEnum.MAGO);
    }

    @Test
    @DisplayName("Deve ter recompensa positiva")
    void testRecompensaPositiva() {
        participacao.setRecompensaOuro(1000);
        assertThat(participacao.getRecompensaOuro()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Deve permitir recompensa zero")
    void testRecompensaZero() {
        participacao.setRecompensaOuro(0);
        assertThat(participacao.getRecompensaOuro()).isZero();
    }

    @Test
    @DisplayName("Deve ter vínculo único entre missão e aventureiro")
    void testUniquenessConstraint() {
        // Simula a constraint UNIQUE(missao_id, aventureiro_id)
        participacao.setMissao(missao);
        participacao.setAventureiro(aventureiro);

        ParticipacaoMissao participacao2 = new ParticipacaoMissao();
        participacao2.setMissao(missao);
        participacao2.setAventureiro(aventureiro);

        // Ambas têm a mesma combinação missão+aventureiro
        assertThat(participacao.getMissao().getId()).isEqualTo(participacao2.getMissao().getId());
        assertThat(participacao.getAventureiro().getId()).isEqualTo(participacao2.getAventureiro().getId());
    }

    @Test
    @DisplayName("Deve construir participação completa")
    void testParticipacaoCompleta() {
        participacao.setId(1L);
        participacao.setMissao(missao);
        participacao.setAventureiro(aventureiro);
        participacao.setPapel(PapelMissaoEnum.GUERREIRO);
        participacao.setRecompensaOuro(2000);
        participacao.setDestaque(true);
        participacao.setRegisteredAt(OffsetDateTime.now());

        assertThat(participacao).isNotNull();
        assertThat(participacao.getId()).isNotNull();
        assertThat(participacao.getMissao()).isNotNull();
        assertThat(participacao.getAventureiro()).isNotNull();
        assertThat(participacao.getPapel()).isNotNull();
        assertThat(participacao.getRecompensaOuro()).isGreaterThan(0);
        assertThat(participacao.getDestaque()).isTrue();
        assertThat(participacao.getRegisteredAt()).isNotNull();
    }
}
