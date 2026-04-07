package com.guilda.registro.audit.repository;

import com.guilda.registro.audit.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;



public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByOrganizacaoIdAndEmail(Long organizacaoId, String email);

    // Busca um usuário já carregando as roles e as permissões (evita LazyInitialization)
    @Query("SELECT u FROM Usuario u " +
            "LEFT JOIN FETCH u.roles r " +
            "LEFT JOIN FETCH r.permissions " +
            "WHERE u.id = :id")
    Optional<Usuario> findByIdWithRolesAndPermissions(@Param("id") Long id);
}