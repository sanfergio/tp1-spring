package com.guilda.registro.audit.repository;

import com.guilda.registro.audit.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}