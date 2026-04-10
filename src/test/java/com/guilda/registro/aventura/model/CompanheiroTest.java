package com.guilda.registro.aventura.model;

import com.guilda.registro.audit.model.Organizacao;
import com.guilda.registro.audit.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Testes da Entidade Companheiro")
class CompanheiroTest {

    private Companheiro companheiro;
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
        aventureiro.setOrganizacao(organizacao);
        aventureiro.setUsuarioCadastro(usuario);

        companheiro = new Companheiro();
    }

    @Test
    @DisplayName("Deve criar um companheiro com valores corretos")
    void testCreateCompanheiro() {
        companheiro.setId(1L);
        companheiro.setNome("Arwen");
        companheiro.setEspecie(EspecieEnum.ELFA);
        companheiro.setLealdade(100);
        companheiro.setAventureiro(aventureiro);

        assertThat(companheiro.getId()).isEqualTo(1L);
        assertThat(companheiro.getNome()).isEqualTo("Arwen");
        assertThat(companheiro.getEspecie()).isEqualTo(EspecieEnum.ELFA);
        assertThat(companheiro.getLealdade()).isEqualTo(100);
        assertThat(companheiro.getAventureiro()).isEqualTo(aventureiro);
    }

    @Test
    @DisplayName("Deve permitir diferentes espécies de companheiro")
    void testDiferentesEspecies() {
        companheiro.setEspecie(EspecieEnum.CACHORRO);
        assertThat(companheiro.getEspecie()).isEqualTo(EspecieEnum.CACHORRO);

        companheiro.setEspecie(EspecieEnum.GATO);
        assertThat(companheiro.getEspecie()).isEqualTo(EspecieEnum.GATO);

        companheiro.setEspecie(EspecieEnum.CAVALO);
        assertThat(companheiro.getEspecie()).isEqualTo(EspecieEnum.CAVALO);

        companheiro.setEspecie(EspecieEnum.DRAGAOZINHO);
        assertThat(companheiro.getEspecie()).isEqualTo(EspecieEnum.DRAGAOZINHO);

        companheiro.setEspecie(EspecieEnum.ELFA);
        assertThat(companheiro.getEspecie()).isEqualTo(EspecieEnum.ELFA);
    }

    @Test
    @DisplayName("Deve permitir lealdade de 0 a 100")
    void testLealdadeValida() {
        for (int lealdade = 0; lealdade <= 100; lealdade += 10) {
            companheiro.setLealdade(lealdade);
            assertThat(companheiro.getLealdade()).isEqualTo(lealdade);
        }
    }

    @Test
    @DisplayName("Deve permitir lealdade mínima de 0")
    void testLealdadeMinima() {
        companheiro.setLealdade(0);
        assertThat(companheiro.getLealdade()).isEqualTo(0);
    }

    @Test
    @DisplayName("Deve permitir lealdade máxima de 100")
    void testLealdadeMaxima() {
        companheiro.setLealdade(100);
        assertThat(companheiro.getLealdade()).isEqualTo(100);
    }

    @Test
    @DisplayName("Deve ter mesmo ID do aventureiro (relacionamento 1-1)")
    void testIdMesmoDoAventureiro() {
        companheiro.setId(1L);
        aventureiro.setId(1L);
        companheiro.setAventureiro(aventureiro);

        assertThat(companheiro.getId()).isEqualTo(aventureiro.getId());
    }

    @Test
    @DisplayName("Deve associar companheiro a um aventureiro")
    void testAssociarAventureiro() {
        assertThat(companheiro.getAventureiro()).isNull();

        companheiro.setAventureiro(aventureiro);
        assertThat(companheiro.getAventureiro()).isEqualTo(aventureiro);
        assertThat(companheiro.getAventureiro().getNome()).isEqualTo("Aragorn");
    }

    @Test
    @DisplayName("Deve permitir nome de companheiro")
    void testNomeCompanheiro() {
        companheiro.setNome("Cavalo Branco");
        assertThat(companheiro.getNome()).isEqualTo("Cavalo Branco");
    }

    @Test
    @DisplayName("Deve permitir atualizar nome do companheiro")
    void testAtualizarNome() {
        companheiro.setNome("Nome Inicial");
        assertThat(companheiro.getNome()).isEqualTo("Nome Inicial");

        companheiro.setNome("Nome Atualizado");
        assertThat(companheiro.getNome()).isEqualTo("Nome Atualizado");
    }

    @Test
    @DisplayName("Deve permitir atualizar lealdade")
    void testAtualizarLealdade() {
        companheiro.setLealdade(50);
        assertThat(companheiro.getLealdade()).isEqualTo(50);

        companheiro.setLealdade(75);
        assertThat(companheiro.getLealdade()).isEqualTo(75);
    }

    @Test
    @DisplayName("Deve permitir nome de até 120 caracteres")
    void testNomeComprimento() {
        String nomeLongo = "a".repeat(120);
        companheiro.setNome(nomeLongo);

        assertThat(companheiro.getNome()).hasSize(120);
    }

    @Test
    @DisplayName("Deve permitir diferentes lealdades para caracterizar comportamento")
    void testLealdadeCaracteriza() {
        // Lealdade baixa
        companheiro.setLealdade(25);
        assertThat(companheiro.getLealdade()).isLessThan(50);

        // Lealdade média
        companheiro.setLealdade(50);
        assertThat(companheiro.getLealdade()).isEqualTo(50);

        // Lealdade alta
        companheiro.setLealdade(75);
        assertThat(companheiro.getLealdade()).isGreaterThan(50);

        // Lealdade máxima
        companheiro.setLealdade(100);
        assertThat(companheiro.getLealdade()).isEqualTo(100);
    }
}
