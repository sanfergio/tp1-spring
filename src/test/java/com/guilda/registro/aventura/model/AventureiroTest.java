package com.guilda.registro.aventura.model;

import com.guilda.registro.audit.model.Organizacao;
import com.guilda.registro.audit.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Testes da Entidade Aventureiro")
class AventureiroTest {

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
    }

    @Test
    @DisplayName("Deve criar um aventureiro com valores corretos")
    void testCreateAventureiro() {
        aventureiro.setId(1L);
        aventureiro.setNome("Aragorn");
        aventureiro.setClasse(ClasseEnum.GUERREIRO);
        aventureiro.setNivel(5);
        aventureiro.setAtivo(true);
        aventureiro.setOrganizacao(organizacao);
        aventureiro.setUsuarioCadastro(usuario);

        assertThat(aventureiro.getId()).isEqualTo(1L);
        assertThat(aventureiro.getNome()).isEqualTo("Aragorn");
        assertThat(aventureiro.getClasse()).isEqualTo(ClasseEnum.GUERREIRO);
        assertThat(aventureiro.getNivel()).isEqualTo(5);
        assertThat(aventureiro.getAtivo()).isTrue();
        assertThat(aventureiro.getOrganizacao()).isEqualTo(organizacao);
        assertThat(aventureiro.getUsuarioCadastro()).isEqualTo(usuario);
    }

    @Test
    @DisplayName("Deve inicializar com nível padrão de 1")
    void testNivelPadrao() {
        aventureiro.setNome("Frodo");

        assertThat(aventureiro.getNivel()).isEqualTo(1);
    }

    @Test
    @DisplayName("Deve inicializar como ativo")
    void testAtivoPadrao() {
        assertThat(aventureiro.getAtivo()).isTrue();
    }

    @Test
    @DisplayName("Deve permitir diferentes classes")
    void testDiferentesClasses() {
        aventureiro.setClasse(ClasseEnum.MAGO);
        assertThat(aventureiro.getClasse()).isEqualTo(ClasseEnum.MAGO);

        aventureiro.setClasse(ClasseEnum.ARQUEIRO);
        assertThat(aventureiro.getClasse()).isEqualTo(ClasseEnum.ARQUEIRO);

        aventureiro.setClasse(ClasseEnum.CLERIGO);
        assertThat(aventureiro.getClasse()).isEqualTo(ClasseEnum.CLERIGO);
    }

    @Test
    @DisplayName("Deve permitir múltiplos níveis")
    void testMultiplosNiveis() {
        for (int nivel = 1; nivel <= 20; nivel++) {
            aventureiro.setNivel(nivel);
            assertThat(aventureiro.getNivel()).isEqualTo(nivel);
        }
    }

    @Test
    @DisplayName("Deve ter companheiro opcionalmente")
    void testCompanheiro() {
        Companheiro companheiro = new Companheiro();
        companheiro.setId(1L);
        companheiro.setNome("Cavalo");

        assertThat(aventureiro.getCompanheiro()).isNull();

        aventureiro.setCompanheiro(companheiro);
        assertThat(aventureiro.getCompanheiro()).isNotNull();
        assertThat(aventureiro.getCompanheiro().getNome()).isEqualTo("Cavalo");
    }

    @Test
    @DisplayName("Deve inicializar com lista vazia de participações")
    void testParticipacoesList() {
        assertThat(aventureiro.getParticipacoes()).isNotNull();
        assertThat(aventureiro.getParticipacoes()).isEmpty();
    }

    @Test
    @DisplayName("Deve permitir adicionar participações")
    void testAdicionarParticipacoes() {
        ParticipacaoMissao participacao = new ParticipacaoMissao();
        participacao.setId(1L);
        participacao.setAventureiro(aventureiro);

        aventureiro.getParticipacoes().add(participacao);

        assertThat(aventureiro.getParticipacoes()).hasSize(1);
        assertThat(aventureiro.getParticipacoes().get(0).getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Deve desativar um aventureiro")
    void testDesativarAventureiro() {
        aventureiro.setAtivo(true);
        assertThat(aventureiro.getAtivo()).isTrue();

        aventureiro.setAtivo(false);
        assertThat(aventureiro.getAtivo()).isFalse();
    }

    @Test
    @DisplayName("Deve registrar os timestamps de criação e atualização")
    void testTimestamps() {
        OffsetDateTime agora = OffsetDateTime.now();
        aventureiro.setCreatedAt(agora);
        aventureiro.setUpdatedAt(agora);

        assertThat(aventureiro.getCreatedAt()).isEqualTo(agora);
        assertThat(aventureiro.getUpdatedAt()).isEqualTo(agora);
    }

    @Test
    @DisplayName("Deve permitir atualizar o timestamp de atualização")
    void testAtualizarTimestamp() {
        OffsetDateTime criacaoTime = OffsetDateTime.now();
        aventureiro.setCreatedAt(criacaoTime);
        aventureiro.setUpdatedAt(criacaoTime);

        OffsetDateTime atualizacaoTime = criacaoTime.plusDays(1);
        aventureiro.setUpdatedAt(atualizacaoTime);

        assertThat(aventureiro.getCreatedAt()).isEqualTo(criacaoTime);
        assertThat(aventureiro.getUpdatedAt()).isEqualTo(atualizacaoTime);
    }

    @Test
    @DisplayName("Deve associar aventureiro a uma organização")
    void testAssociarOrganizacao() {
        assertThat(aventureiro.getOrganizacao()).isNull();

        aventureiro.setOrganizacao(organizacao);
        assertThat(aventureiro.getOrganizacao()).isEqualTo(organizacao);
        assertThat(aventureiro.getOrganizacao().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Deve associar aventureiro a um usuário de cadastro")
    void testAssociarUsuario() {
        assertThat(aventureiro.getUsuarioCadastro()).isNull();

        aventureiro.setUsuarioCadastro(usuario);
        assertThat(aventureiro.getUsuarioCadastro()).isEqualTo(usuario);
        assertThat(aventureiro.getUsuarioCadastro().getId()).isEqualTo(1L);
    }
}
