package com.guilda.registro.audit.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Testes da Entidade Role")
class RoleTest {

    private Role role;
    private Organizacao organizacao;

    @BeforeEach
    void setUp() {
        organizacao = new Organizacao();
        organizacao.setId(1L);

        role = new Role();
    }

    @Test
    @DisplayName("Deve criar uma role com valores corretos")
    void testCreateRole() {
        role.setId(1L);
        role.setNome("ADMIN");
        role.setDescricao("Administrador do sistema");
        role.setOrganizacao(organizacao);

        assertThat(role.getId()).isEqualTo(1L);
        assertThat(role.getNome()).isEqualTo("ADMIN");
        assertThat(role.getDescricao()).isEqualTo("Administrador do sistema");
        assertThat(role.getOrganizacao()).isEqualTo(organizacao);
    }

    @Test
    @DisplayName("Deve inicializar com conjunto vazio de permissions")
    void testPermissionsVazias() {
        assertThat(role.getPermissions()).isNotNull();
        assertThat(role.getPermissions()).isEmpty();
    }

    @Test
    @DisplayName("Deve permitir adicionar permissions")
    void testAdicionarPermessions() {
        Permission permission = new Permission();
        permission.setId(1L);
        permission.setNome("READ");

        role.getPermissions().add(permission);

        assertThat(role.getPermissions()).hasSize(1);
        assertThat(role.getPermissions()).contains(permission);
    }

    @Test
    @DisplayName("Deve permitir múltiplas permissions")
    void testMultiplasPermissions() {
        Permission permissionRead = new Permission();
        permissionRead.setId(1L);
        permissionRead.setNome("READ");

        Permission permissionWrite = new Permission();
        permissionWrite.setId(2L);
        permissionWrite.setNome("WRITE");

        role.getPermissions().add(permissionRead);
        role.getPermissions().add(permissionWrite);

        assertThat(role.getPermissions()).hasSize(2);
        assertThat(role.getPermissions()).contains(permissionRead, permissionWrite);
    }

    @Test
    @DisplayName("Deve permitir remover permission")
    void testRemoverPermission() {
        Permission permission = new Permission();
        permission.setId(1L);
        permission.setNome("READ");

        role.getPermissions().add(permission);
        assertThat(role.getPermissions()).hasSize(1);

        role.getPermissions().remove(permission);
        assertThat(role.getPermissions()).isEmpty();
    }

    @Test
    @DisplayName("Deve registrar o timestamp de criação")
    void testCreatedAt() {
        OffsetDateTime agora = OffsetDateTime.now();
        role.setCreatedAt(agora);

        assertThat(role.getCreatedAt()).isEqualTo(agora);
    }

    @Test
    @DisplayName("Deve inicializar com conjunto vazio de usuários")
    void testUsuariosVazios() {
        assertThat(role.getUsuarios()).isNotNull();
        assertThat(role.getUsuarios()).isEmpty();
    }

    @Test
    @DisplayName("Deve associar role a uma organização")
    void testAssociarOrganizacao() {
        assertThat(role.getOrganizacao()).isNull();

        role.setOrganizacao(organizacao);
        assertThat(role.getOrganizacao()).isEqualTo(organizacao);
        assertThat(role.getOrganizacao().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Deve permitir descrição vazia")
    void testDescricaoVazia() {
        role.setDescricao("");
        assertThat(role.getDescricao()).isEmpty();
    }

    @Test
    @DisplayName("Deve permitir descrição nula")
    void testDescricaoNula() {
        assertThat(role.getDescricao()).isNull();
    }

    @Test
    @DisplayName("Deve permitir diferentes nomes de role")
    void testDiferentesNomes() {
        role.setNome("ADMIN");
        assertThat(role.getNome()).isEqualTo("ADMIN");

        role.setNome("USER");
        assertThat(role.getNome()).isEqualTo("USER");

        role.setNome("GUEST");
        assertThat(role.getNome()).isEqualTo("GUEST");
    }

    @Test
    @DisplayName("Deve usar Set para garantir permissions únicas")
    void testPermissionsUniques() {
        Permission permission = new Permission();
        permission.setId(1L);
        permission.setNome("READ");

        role.getPermissions().add(permission);
        role.getPermissions().add(permission);

        // Set deve manter apenas uma instância
        assertThat(role.getPermissions()).hasSize(1);
    }
}
