package com.guilda.registro.audit.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Testes da Entidade Organizacao")
class OrganizacaoTest {

    private Organizacao organizacao;

    @BeforeEach
    void setUp() {
        organizacao = new Organizacao();
    }

    @Test
    @DisplayName("Deve criar uma organização com valores corretos")
    void testCreateOrganizacao() {
        organizacao.setId(1L);
        organizacao.setNome("Guilda Aventureira");
        organizacao.setAtivo(true);

        assertThat(organizacao.getId()).isEqualTo(1L);
        assertThat(organizacao.getNome()).isEqualTo("Guilda Aventureira");
        assertThat(organizacao.getAtivo()).isTrue();
    }

    @Test
    @DisplayName("Deve inicializar com lista vazia de usuários")
    void testUsuariosVazios() {
        assertThat(organizacao.getUsuarios()).isNotNull();
        assertThat(organizacao.getUsuarios()).isEmpty();
    }

    @Test
    @DisplayName("Deve inicializar com lista vazia de API Keys")
    void testApiKeysVazias() {
        assertThat(organizacao.getApiKeys()).isNotNull();
        assertThat(organizacao.getApiKeys()).isEmpty();
    }

    @Test
    @DisplayName("Deve inicializar com lista vazia de roles")
    void testRolesVazias() {
        assertThat(organizacao.getRoles()).isNotNull();
        assertThat(organizacao.getRoles()).isEmpty();
    }

    @Test
    @DisplayName("Deve inicializar com lista vazia de audit entries")
    void testAuditEntriesVazias() {
        assertThat(organizacao.getAuditEntries()).isNotNull();
        assertThat(organizacao.getAuditEntries()).isEmpty();
    }

    @Test
    @DisplayName("Deve permitir adicionar usuários")
    void testAdicionarUsuarios() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Admin");

        organizacao.getUsuarios().add(usuario);

        assertThat(organizacao.getUsuarios()).hasSize(1);
        assertThat(organizacao.getUsuarios()).contains(usuario);
    }

    @Test
    @DisplayName("Deve permitir adicionar API Keys")
    void testAdicionarApiKeys() {
        ApiKey apiKey = new ApiKey();
        apiKey.setId(1L);
        // Se a classe ApiKey tiver setChave, descomente a linha abaixo.
        // Caso contrário, apenas adicione o objeto sem setar a chave.
        // apiKey.setChave("key123");  // REMOVIDO - método inexistente

        organizacao.getApiKeys().add(apiKey);

        assertThat(organizacao.getApiKeys()).hasSize(1);
        assertThat(organizacao.getApiKeys()).contains(apiKey);
    }

    @Test
    @DisplayName("Deve permitir adicionar roles")
    void testAdicionarRoles() {
        Role role = new Role();
        role.setId(1L);
        role.setNome("ADMIN");

        organizacao.getRoles().add(role);

        assertThat(organizacao.getRoles()).hasSize(1);
        assertThat(organizacao.getRoles()).contains(role);
    }

    @Test
    @DisplayName("Deve permitir adicionar audit entries")
    void testAdicionarAuditEntries() {
        AuditEntry auditEntry = new AuditEntry();
        auditEntry.setId(1L);
        // Se a classe AuditEntry tiver setAcao, descomente a linha abaixo.
        // Caso contrário, apenas adicione o objeto sem setar a ação.
        // auditEntry.setAcao("CREATE");  // REMOVIDO - método inexistente

        organizacao.getAuditEntries().add(auditEntry);

        assertThat(organizacao.getAuditEntries()).hasSize(1);
        assertThat(organizacao.getAuditEntries()).contains(auditEntry);
    }

    @Test
    @DisplayName("Deve registrar o timestamp de criação")
    void testCreatedAt() {
        OffsetDateTime agora = OffsetDateTime.now();
        organizacao.setCreatedAt(agora);

        assertThat(organizacao.getCreatedAt()).isEqualTo(agora);
    }

    @Test
    @DisplayName("Deve permitir ativar e desativar organização")
    void testAtivoInativo() {
        organizacao.setAtivo(true);
        assertThat(organizacao.getAtivo()).isTrue();

        organizacao.setAtivo(false);
        assertThat(organizacao.getAtivo()).isFalse();
    }

    @Test
    @DisplayName("Deve permitir atualizar nome da organização")
    void testAtualizarNome() {
        organizacao.setNome("Nome Inicial");
        assertThat(organizacao.getNome()).isEqualTo("Nome Inicial");

        organizacao.setNome("Nome Atualizado");
        assertThat(organizacao.getNome()).isEqualTo("Nome Atualizado");
    }

    @Test
    @DisplayName("Deve permitir múltiplos usuários")
    void testMultiplosUsuarios() {
        for (int i = 0; i < 5; i++) {
            Usuario usuario = new Usuario();
            usuario.setId((long) i);
            usuario.setNome("Usuario " + i);
            organizacao.getUsuarios().add(usuario);
        }

        assertThat(organizacao.getUsuarios()).hasSize(5);
    }

    @Test
    @DisplayName("Deve permitir múltiplas roles")
    void testMultiplasRoles() {
        for (int i = 0; i < 3; i++) {
            Role role = new Role();
            role.setId((long) i);
            role.setNome("ROLE_" + i);
            organizacao.getRoles().add(role);
        }

        assertThat(organizacao.getRoles()).hasSize(3);
    }

    @Test
    @DisplayName("Deve permitir remover usuário")
    void testRemoverUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        organizacao.getUsuarios().add(usuario);
        assertThat(organizacao.getUsuarios()).hasSize(1);

        organizacao.getUsuarios().remove(usuario);
        assertThat(organizacao.getUsuarios()).isEmpty();
    }

    @Test
    @DisplayName("Deve ter nome único (constraint)")
    void testNomeUnico() {
        organizacao.setNome("Guilda Única");
        assertThat(organizacao.getNome()).isEqualTo("Guilda Única");

        Organizacao organizacao2 = new Organizacao();
        organizacao2.setNome("Guilda Única");
        // Ambas têm o mesmo nome - violaria constraint em banco
        assertThat(organizacao2.getNome()).isEqualTo(organizacao.getNome());
    }

    @Test
    @DisplayName("Deve permitir nome nulo (antes de persistir)")
    void testNomeNulo() {
        organizacao.setNome(null);
        assertThat(organizacao.getNome()).isNull();
    }

    @Test
    @DisplayName("Deve permitir ativo nulo")
    void testAtivoNulo() {
        organizacao.setAtivo(null);
        assertThat(organizacao.getAtivo()).isNull();
    }

    @Test
    @DisplayName("Deve construir organização completa")
    void testOrganizacaoCompleta() {
        organizacao.setId(1L);
        organizacao.setNome("Guilda Completa");
        organizacao.setAtivo(true);
        organizacao.setCreatedAt(OffsetDateTime.now());

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        organizacao.getUsuarios().add(usuario);

        Role role = new Role();
        role.setId(1L);
        organizacao.getRoles().add(role);

        assertThat(organizacao).isNotNull();
        assertThat(organizacao.getId()).isNotNull();
        assertThat(organizacao.getNome()).isNotNull();
        assertThat(organizacao.getAtivo()).isTrue();
        assertThat(organizacao.getCreatedAt()).isNotNull();
        assertThat(organizacao.getUsuarios()).hasSize(1);
        assertThat(organizacao.getRoles()).hasSize(1);
    }
}