package com.guilda.registro.audit.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Testes da Entidade Usuario")
class UsuarioTest {

    private Usuario usuario;
    private Organizacao organizacao;

    @BeforeEach
    void setUp() {
        organizacao = new Organizacao();
        organizacao.setId(1L);

        usuario = new Usuario();
    }

    @Test
    @DisplayName("Deve criar um usuário com valores corretos")
    void testCreateUsuario() {
        usuario.setId(1L);
        usuario.setNome("Admin Usuário");
        usuario.setEmail("admin@test.com");
        usuario.setSenhaHash("hash_da_senha");
        usuario.setStatus("ATIVO");
        usuario.setOrganizacao(organizacao);

        assertThat(usuario.getId()).isEqualTo(1L);
        assertThat(usuario.getNome()).isEqualTo("Admin Usuário");
        assertThat(usuario.getEmail()).isEqualTo("admin@test.com");
        assertThat(usuario.getSenhaHash()).isEqualTo("hash_da_senha");
        assertThat(usuario.getStatus()).isEqualTo("ATIVO");
        assertThat(usuario.getOrganizacao()).isEqualTo(organizacao);
    }

    @Test
    @DisplayName("Deve inicializar com conjunto vazio de roles")
    void testRolesVazias() {
        assertThat(usuario.getRoles()).isNotNull();
        assertThat(usuario.getRoles()).isEmpty();
    }

    @Test
    @DisplayName("Deve permitir adicionar roles")
    void testAdicionarRoles() {
        Role role = new Role();
        role.setId(1L);
        role.setNome("ADMIN");

        usuario.getRoles().add(role);

        assertThat(usuario.getRoles()).hasSize(1);
        assertThat(usuario.getRoles()).contains(role);
    }

    @Test
    @DisplayName("Deve permitir múltiplas roles")
    void testMultiplasRoles() {
        Role roleAdmin = new Role();
        roleAdmin.setId(1L);
        roleAdmin.setNome("ADMIN");

        Role roleUser = new Role();
        roleUser.setId(2L);
        roleUser.setNome("USER");

        usuario.getRoles().add(roleAdmin);
        usuario.getRoles().add(roleUser);

        assertThat(usuario.getRoles()).hasSize(2);
        assertThat(usuario.getRoles()).contains(roleAdmin, roleUser);
    }

    @Test
    @DisplayName("Deve permitir remover role")
    void testRemoverRole() {
        Role role = new Role();
        role.setId(1L);
        role.setNome("ADMIN");

        usuario.getRoles().add(role);
        assertThat(usuario.getRoles()).hasSize(1);

        usuario.getRoles().remove(role);
        assertThat(usuario.getRoles()).isEmpty();
    }

    @Test
    @DisplayName("Deve registrar o último login")
    void testUltimoLogin() {
        OffsetDateTime ultimoLogin = OffsetDateTime.now();
        usuario.setUltimoLoginEm(ultimoLogin);

        assertThat(usuario.getUltimoLoginEm()).isEqualTo(ultimoLogin);
    }

    @Test
    @DisplayName("Deve permitir último login nulo")
    void testUltimoLoginNulo() {
        assertThat(usuario.getUltimoLoginEm()).isNull();
    }

    @Test
    @DisplayName("Deve registrar timestamps de criação e atualização")
    void testTimestamps() {
        OffsetDateTime agora = OffsetDateTime.now();
        usuario.setCreatedAt(agora);
        usuario.setUpdatedAt(agora);

        assertThat(usuario.getCreatedAt()).isEqualTo(agora);
        assertThat(usuario.getUpdatedAt()).isEqualTo(agora);
    }

    @Test
    @DisplayName("Deve permitir atualizar o timestamp de atualização")
    void testAtualizarTimestamp() {
        OffsetDateTime criacaoTime = OffsetDateTime.now();
        usuario.setCreatedAt(criacaoTime);
        usuario.setUpdatedAt(criacaoTime);

        OffsetDateTime atualizacaoTime = criacaoTime.plusDays(1);
        usuario.setUpdatedAt(atualizacaoTime);

        assertThat(usuario.getCreatedAt()).isEqualTo(criacaoTime);
        assertThat(usuario.getUpdatedAt()).isEqualTo(atualizacaoTime);
    }

    @Test
    @DisplayName("Deve associar usuário a uma organização")
    void testAssociarOrganizacao() {
        assertThat(usuario.getOrganizacao()).isNull();

        usuario.setOrganizacao(organizacao);
        assertThat(usuario.getOrganizacao()).isEqualTo(organizacao);
        assertThat(usuario.getOrganizacao().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Deve permitir diferentes status de usuário")
    void testStatusDiferentes() {
        usuario.setStatus("ATIVO");
        assertThat(usuario.getStatus()).isEqualTo("ATIVO");

        usuario.setStatus("INATIVO");
        assertThat(usuario.getStatus()).isEqualTo("INATIVO");

        usuario.setStatus("BLOQUEADO");
        assertThat(usuario.getStatus()).isEqualTo("BLOQUEADO");
    }

    @Test
    @DisplayName("Deve permitir email válido")
    void testEmailValido() {
        usuario.setEmail("usuario@example.com");
        assertThat(usuario.getEmail()).isEqualTo("usuario@example.com");
    }

    @Test
    @DisplayName("Deve permitir email nulo")
    void testEmailNulo() {
        assertThat(usuario.getEmail()).isNull();
    }

    @Test
    @DisplayName("Deve permitir atualizar nome do usuário")
    void testAtualizarNome() {
        usuario.setNome("Nome Inicial");
        assertThat(usuario.getNome()).isEqualTo("Nome Inicial");

        usuario.setNome("Nome Atualizado");
        assertThat(usuario.getNome()).isEqualTo("Nome Atualizado");
    }

    @Test
    @DisplayName("Deve permitir atualizar senha hash")
    void testAtualizarSenhaHash() {
        usuario.setSenhaHash("hash_inicial");
        assertThat(usuario.getSenhaHash()).isEqualTo("hash_inicial");

        usuario.setSenhaHash("hash_novo");
        assertThat(usuario.getSenhaHash()).isEqualTo("hash_novo");
    }

    @Test
    @DisplayName("Deve usar Set para garantir roles únicas")
    void testRolesUniques() {
        Role role = new Role();
        role.setId(1L);
        role.setNome("ADMIN");

        usuario.getRoles().add(role);
        usuario.getRoles().add(role);

        // Set deve manter apenas uma instância
        assertThat(usuario.getRoles()).hasSize(1);
    }

    @Test
    @DisplayName("Deve permitir interagir com usuário sem organização inicialmente")
    void testUsuarioSemOrganizacao() {
        usuario.setNome("Usuário Teste");
        usuario.setEmail("teste@example.com");

        assertThat(usuario.getNome()).isEqualTo("Usuário Teste");
        assertThat(usuario.getEmail()).isEqualTo("teste@example.com");
        assertThat(usuario.getOrganizacao()).isNull();
    }
}
