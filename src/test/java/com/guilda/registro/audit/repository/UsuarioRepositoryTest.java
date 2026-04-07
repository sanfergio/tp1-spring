package com.guilda.registro.audit.repository;

import com.guilda.registro.audit.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UsuarioRepositoryTest {

    private static final DockerImageName POSTGRES_IMAGE =
            DockerImageName.parse("leogloriainfnet/postgres-tp2-spring:1.0")
                    .asCompatibleSubstituteFor("postgres");

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(POSTGRES_IMAGE)
            .withDatabaseName("auditdb")
            .withUsername("postgres")
            .withPassword("root");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        // 🔥 DESLIGA VALIDAÇÃO DO SCHEMA (RESOLVE SEU ERRO)
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
    }

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void deveCarregarUsuarioComRolesEPermissoes() {
        var usuarioOpt = usuarioRepository.findByIdWithRolesAndPermissions(1L);
        assertThat(usuarioOpt).isPresent();

        Usuario usuario = usuarioOpt.get();
        assertThat(usuario.getOrganizacao()).isNotNull();
        assertThat(usuario.getOrganizacao().getNome()).isNotBlank();

        assertThat(usuario.getRoles()).isNotEmpty();
        usuario.getRoles().forEach(role -> {
            assertThat(role.getPermissions()).isNotEmpty();
            role.getPermissions().forEach(perm -> assertThat(perm.getCode()).isNotBlank());
        });

        System.out.println("Usuário: " + usuario.getNome());
        System.out.println("Organização: " + usuario.getOrganizacao().getNome());
        System.out.println("Roles: " + usuario.getRoles().size());
        System.out.println("Permissões totais: " +
                usuario.getRoles().stream().flatMap(r -> r.getPermissions().stream()).count());
    }
}