package com.guilda.registro.aventura.model;

import com.guilda.registro.audit.model.Organizacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Testes da Entidade Missao")
class MissaoTest {

    private Missao missao;
    private Organizacao organizacao;

    @BeforeEach
    void setUp() {
        organizacao = new Organizacao();
        organizacao.setId(1L);

        missao = new Missao();
    }

    @Test
    @DisplayName("Deve criar uma missão com valores corretos")
    void testCreateMissao() {
        missao.setId(1L);
        missao.setTitulo("Derrotar o Dragão");
        missao.setNivelPerigo(NivelPerigoEnum.ALTO);
        missao.setStatus(StatusMissaoEnum.PLANEJADA);
        missao.setOrganizacao(organizacao);
        missao.setDataInicio(LocalDate.now());
        missao.setDataTermino(LocalDate.now().plusDays(7));

        assertThat(missao.getId()).isEqualTo(1L);
        assertThat(missao.getTitulo()).isEqualTo("Derrotar o Dragão");
        assertThat(missao.getNivelPerigo()).isEqualTo(NivelPerigoEnum.ALTO);
        assertThat(missao.getStatus()).isEqualTo(StatusMissaoEnum.PLANEJADA);
        assertThat(missao.getOrganizacao()).isEqualTo(organizacao);
    }

    @Test
    @DisplayName("Deve inicializar com status PLANEJADA")
    void testStatusPadrao() {
        assertThat(missao.getStatus()).isEqualTo(StatusMissaoEnum.PLANEJADA);
    }

    @Test
    @DisplayName("Deve permitir diferentes níveis de perigo")
    void testDiferentesNiveisCPerigo() {
        missao.setNivelPerigo(NivelPerigoEnum.BAIXO);
        assertThat(missao.getNivelPerigo()).isEqualTo(NivelPerigoEnum.BAIXO);

        missao.setNivelPerigo(NivelPerigoEnum.MEDIO);
        assertThat(missao.getNivelPerigo()).isEqualTo(NivelPerigoEnum.MEDIO);

        missao.setNivelPerigo(NivelPerigoEnum.ALTO);
        assertThat(missao.getNivelPerigo()).isEqualTo(NivelPerigoEnum.ALTO);

        missao.setNivelPerigo(NivelPerigoEnum.CRITICO);
        assertThat(missao.getNivelPerigo()).isEqualTo(NivelPerigoEnum.CRITICO);
    }

    @Test
    @DisplayName("Deve permitir diferentes status")
    void testDiferentesStatus() {
        missao.setStatus(StatusMissaoEnum.PLANEJADA);
        assertThat(missao.getStatus()).isEqualTo(StatusMissaoEnum.PLANEJADA);

        missao.setStatus(StatusMissaoEnum.EM_ANDAMENTO);
        assertThat(missao.getStatus()).isEqualTo(StatusMissaoEnum.EM_ANDAMENTO);

        missao.setStatus(StatusMissaoEnum.CONCLUIDA);
        assertThat(missao.getStatus()).isEqualTo(StatusMissaoEnum.CONCLUIDA);

        missao.setStatus(StatusMissaoEnum.CANCELADA);
        assertThat(missao.getStatus()).isEqualTo(StatusMissaoEnum.CANCELADA);
    }

    @Test
    @DisplayName("Deve definir datas de início e término")
    void testDatas() {
        LocalDate dataInicio = LocalDate.now();
        LocalDate dataTermino = LocalDate.now().plusDays(7);

        missao.setDataInicio(dataInicio);
        missao.setDataTermino(dataTermino);

        assertThat(missao.getDataInicio()).isEqualTo(dataInicio);
        assertThat(missao.getDataTermino()).isEqualTo(dataTermino);
    }

    @Test
    @DisplayName("Deve permitir datas nulas")
    void testDataNulas() {
        assertThat(missao.getDataInicio()).isNull();
        assertThat(missao.getDataTermino()).isNull();
    }

    @Test
    @DisplayName("Deve inicializar com lista vazia de participações")
    void testParticipacoesList() {
        assertThat(missao.getParticipacoes()).isNotNull();
        assertThat(missao.getParticipacoes()).isEmpty();
    }

    @Test
    @DisplayName("Deve permitir adicionar participações")
    void testAdicionarParticipacoes() {
        ParticipacaoMissao participacao = new ParticipacaoMissao();
        participacao.setId(1L);
        participacao.setMissao(missao);

        missao.getParticipacoes().add(participacao);

        assertThat(missao.getParticipacoes()).hasSize(1);
        assertThat(missao.getParticipacoes().get(0).getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Deve registrar o timestamp de criação")
    void testCreatedAt() {
        OffsetDateTime agora = OffsetDateTime.now();
        missao.setCreatedAt(agora);

        assertThat(missao.getCreatedAt()).isEqualTo(agora);
    }

    @Test
    @DisplayName("Deve associar a missão a uma organização")
    void testAssociarOrganizacao() {
        assertThat(missao.getOrganizacao()).isNull();

        missao.setOrganizacao(organizacao);
        assertThat(missao.getOrganizacao()).isEqualTo(organizacao);
        assertThat(missao.getOrganizacao().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Deve permitir múltiplas participações")
    void testMultiplasParticipacoes() {
        for (int i = 1; i <= 5; i++) {
            ParticipacaoMissao participacao = new ParticipacaoMissao();
            participacao.setId((long) i);
            missao.getParticipacoes().add(participacao);
        }

        assertThat(missao.getParticipacoes()).hasSize(5);
    }

    @Test
    @DisplayName("Deve permitir ttulo até 150 caracteres")
    void testTituloComprimento() {
        String tituloLongo = "a".repeat(150);
        missao.setTitulo(tituloLongo);

        assertThat(missao.getTitulo()).hasSize(150);
    }

    @Test
    @DisplayName("Deve permitir título vazio")
    void testTituloVazio() {
        missao.setTitulo("");
        assertThat(missao.getTitulo()).isEmpty();
    }

    @Test
    @DisplayName("Deve transição de status: PLANEJADA -> EM_ANDAMENTO")
    void testTransicaoStatusPlanejadaParaEmAndamento() {
        assertThat(missao.getStatus()).isEqualTo(StatusMissaoEnum.PLANEJADA);

        missao.setStatus(StatusMissaoEnum.EM_ANDAMENTO);
        assertThat(missao.getStatus()).isEqualTo(StatusMissaoEnum.EM_ANDAMENTO);
    }

    @Test
    @DisplayName("Deve transição de status: EM_ANDAMENTO -> CONCLUIDA")
    void testTransicaoStatusEmAndamentoParaConcluida() {
        missao.setStatus(StatusMissaoEnum.EM_ANDAMENTO);
        missao.setStatus(StatusMissaoEnum.CONCLUIDA);

        assertThat(missao.getStatus()).isEqualTo(StatusMissaoEnum.CONCLUIDA);
    }

    @Test
    @DisplayName("Deve transição de status: PLANEJADA -> CANCELADA")
    void testTTransicaoStatusPlanejadaParaCancelada() {
        assertThat(missao.getStatus()).isEqualTo(StatusMissaoEnum.PLANEJADA);

        missao.setStatus(StatusMissaoEnum.CANCELADA);
        assertThat(missao.getStatus()).isEqualTo(StatusMissaoEnum.CANCELADA);
    }
}
