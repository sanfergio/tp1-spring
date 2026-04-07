package com.guilda.registro.audit.controller;

import com.guilda.registro.audit.dto.NovoUsuarioRequest;
import com.guilda.registro.audit.dto.RoleComPermissoesResponse;
import com.guilda.registro.audit.dto.response.UsuarioComRolesResponse;
import com.guilda.registro.audit.model.Role;
import com.guilda.registro.audit.model.Usuario;
import com.guilda.registro.audit.repository.OrganizacaoRepository;
import com.guilda.registro.audit.repository.RoleRepository;
import com.guilda.registro.audit.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/audit")
@RequiredArgsConstructor
public class AuditController {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final OrganizacaoRepository organizacaoRepository;

    @GetMapping("/usuarios")
    public List<UsuarioComRolesResponse> listarUsuariosComRoles() {
        return usuarioRepository.findAll().stream()
                .map(this::toUsuarioResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/roles")
    public List<RoleComPermissoesResponse> listarRolesComPermissoes() {
        return roleRepository.findAll().stream()
                .map(this::toRoleResponse)
                .collect(Collectors.toList());
    }

    @PostMapping("/usuarios")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioComRolesResponse criarUsuario(@Valid @RequestBody NovoUsuarioRequest request) {
        var organizacao = organizacaoRepository.findById(request.getOrganizacaoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Organização não encontrada"));

        // verifica se email já existe na organização (unique constraint)
        if (usuarioRepository.existsByOrganizacaoIdAndEmail(request.getOrganizacaoId(), request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já utilizado nesta organização");
        }

        Usuario usuario = new Usuario();
        usuario.setOrganizacao(organizacao);
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenhaHash(request.getSenhaHash()); // idealmente, hash antes de salvar
        usuario.setStatus(request.getStatus());
        usuario.setCreatedAt(OffsetDateTime.now());
        usuario.setUpdatedAt(OffsetDateTime.now());

        if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            Set<Role> roles = roleRepository.findAllById(request.getRoleIds()).stream()
                    .collect(Collectors.toSet());
            usuario.setRoles(roles);
        }

        Usuario saved = usuarioRepository.save(usuario);
        return toUsuarioResponse(saved);
    }

    private UsuarioComRolesResponse toUsuarioResponse(Usuario u) {
        Set<String> roleNomes = u.getRoles().stream()
                .map(Role::getNome)
                .collect(Collectors.toSet());
        return new UsuarioComRolesResponse(
                u.getId(),
                u.getNome(),
                u.getEmail(),
                u.getStatus(),
                u.getOrganizacao().getNome(),
                roleNomes
        );
    }

    private RoleComPermissoesResponse toRoleResponse(Role r) {
        Set<String> permCodes = r.getPermissions().stream()
                .map(p -> p.getCode())
                .collect(Collectors.toSet());
        return new RoleComPermissoesResponse(
                r.getId(),
                r.getNome(),
                r.getDescricao(),
                r.getOrganizacao().getNome(),
                permCodes
        );
    }
}