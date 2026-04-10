package com.guilda.registro.aventura.service;
import com.guilda.registro.aventura.controller.AventureiroResponse;
import com.guilda.registro.aventura.dto.AventureiroCreateRequest;
import com.guilda.registro.aventura.dto.CompanheiroRequest;
import com.guilda.registro.aventura.model.Aventureiro;
import com.guilda.registro.aventura.model.ClasseEnum;
import com.guilda.registro.aventura.model.Companheiro;
import com.guilda.registro.aventura.repository.AventureiroRepository;
import com.guilda.registro.aventura.repository.CompanheiroRepository;
import com.guilda.registro.audit.model.Organizacao;
import com.guilda.registro.audit.model.Usuario;
import com.guilda.registro.audit.repository.OrganizacaoRepository;
import com.guilda.registro.audit.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do AventureiroService")
class AventureiroServiceTest {

    @Mock
    private AventureiroRepository aventureiroRepository;
    
    @Mock
    private CompanheiroRepository companheiroRepository;
    
    @Mock
    private OrganizacaoRepository organizacaoRepository;
    
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AventureiroService aventureiroService;

    private Organizacao organizacao;
    private Usuario usuario;
    private Aventureiro aventureiro;
    private AventureiroCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        organizacao = new Organizacao();
        organizacao.setId(1L);
        organizacao.setNome("Guilda Aventureira");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Admin Usuário");
        usuario.setEmail("admin@test.com");

        aventureiro = new Aventureiro();
        aventureiro.setId(1L);
        aventureiro.setNome("Aragorn");
        aventureiro.setClasse(ClasseEnum.GUERREIRO);
        aventureiro.setNivel(5);
        aventureiro.setAtivo(true);
        aventureiro.setOrganizacao(organizacao);
        aventureiro.setUsuarioCadastro(usuario);
        aventureiro.setCreatedAt(OffsetDateTime.now());
        aventureiro.setUpdatedAt(OffsetDateTime.now());

        createRequest = new AventureiroCreateRequest();
        createRequest.setOrganizacaoId(1L);
        createRequest.setUsuarioCadastroId(1L);
        createRequest.setNome("Aragorn");
        createRequest.setClasse("GUERREIRO");
        createRequest.setNivel(5);
    }

    @Test
    @DisplayName("Deve criar um aventureiro com sucesso")
    void testCriarAventureiro_Sucesso() {
        when(organizacaoRepository.findById(1L)).thenReturn(Optional.of(organizacao));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(aventureiroRepository.save(any(Aventureiro.class))).thenReturn(aventureiro);

        AventureiroResponse response = aventureiroService.criarAventureiro(createRequest);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getNome()).isEqualTo("Aragorn");
        assertThat(response.getClasse()).isEqualTo(ClasseEnum.GUERREIRO);
        assertThat(response.getNivel()).isEqualTo(5);
        assertThat(response.getAtivo()).isTrue();

        verify(organizacaoRepository).findById(1L);
        verify(usuarioRepository).findById(1L);
        verify(aventureiroRepository).save(any(Aventureiro.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar aventureiro com organização inexistente")
    void testCriarAventureiro_OrganizacaoNaoEncontrada() {
        when(organizacaoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> aventureiroService.criarAventureiro(createRequest))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Organização não encontrada");

        verify(organizacaoRepository).findById(1L);
        verify(aventureiroRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar aventureiro com usuário inexistente")
    void testCriarAventureiro_UsuarioNaoEncontrado() {
        when(organizacaoRepository.findById(1L)).thenReturn(Optional.of(organizacao));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> aventureiroService.criarAventureiro(createRequest))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Usuário não encontrado");

        verify(aventureiroRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar aventureiro com classe inválida")
    void testCriarAventureiro_ClasseInvalida() {
        createRequest.setClasse("CLASSE_INVALIDA");
        when(organizacaoRepository.findById(1L)).thenReturn(Optional.of(organizacao));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        assertThatThrownBy(() -> aventureiroService.criarAventureiro(createRequest))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Classe inválida");

        verify(aventureiroRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve usar nível padrão 1 quando nível não fornecido")
    void testCriarAventureiro_NivelPadrao() {
        createRequest.setNivel(null);
        Aventureiro aventureiroComNivelPadrao = new Aventureiro();
        aventureiroComNivelPadrao.setId(1L);
        aventureiroComNivelPadrao.setNome("Aragorn");
        aventureiroComNivelPadrao.setNivel(1);
        aventureiroComNivelPadrao.setClasse(ClasseEnum.GUERREIRO);
        aventureiroComNivelPadrao.setAtivo(true);
        aventureiroComNivelPadrao.setOrganizacao(organizacao);
        aventureiroComNivelPadrao.setUsuarioCadastro(usuario);

        when(organizacaoRepository.findById(1L)).thenReturn(Optional.of(organizacao));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(aventureiroRepository.save(any(Aventureiro.class))).thenReturn(aventureiroComNivelPadrao);

        AventureiroResponse response = aventureiroService.criarAventureiro(createRequest);

        assertThat(response.getNivel()).isEqualTo(1);
    }

    @Test
    @DisplayName("Deve adicionar companheiro a aventureiro existente")
    void testAdicionarCompanheiro_Sucesso() {
        CompanheiroRequest companheiroRequest = new CompanheiroRequest();
        companheiroRequest.setNome("Arwen");
        companheiroRequest.setEspecie("ELFA");
        companheiroRequest.setLealdade(100);

        Companheiro companheiro = new Companheiro();
        companheiro.setId(1L);
        companheiro.setNome("Arwen");
        // companheiro.setEspecie(EspecieEnum.ELFA);
        companheiro.setLealdade(100);
        companheiro.setAventureiro(aventureiro);

        aventureiro.setCompanheiro(companheiro);

        when(aventureiroRepository.findById(1L)).thenReturn(Optional.of(aventureiro));
        when(companheiroRepository.save(any(Companheiro.class))).thenReturn(companheiro);
        when(aventureiroRepository.save(any(Aventureiro.class))).thenReturn(aventureiro);

        AventureiroResponse response = aventureiroService.adicionarCompanheiro(1L, companheiroRequest);

        assertThat(response).isNotNull();
        assertThat(response.getCompanheiro()).isNotNull();
        assertThat(response.getCompanheiro().getNome()).isEqualTo("Arwen");
        assertThat(response.getCompanheiro().getLealdade()).isEqualTo(100);

        verify(aventureiroRepository).findById(1L);
        verify(companheiroRepository).save(any(Companheiro.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao adicionar companheiro com lealdade inválida (negativa)")
    void testAdicionarCompanheiro_LealdadeNegativa() {
        CompanheiroRequest companheiroRequest = new CompanheiroRequest();
        companheiroRequest.setNome("Arwen");
        companheiroRequest.setEspecie("ELFA");
        companheiroRequest.setLealdade(-10);

        when(aventureiroRepository.findById(1L)).thenReturn(Optional.of(aventureiro));

        assertThatThrownBy(() -> aventureiroService.adicionarCompanheiro(1L, companheiroRequest))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Lealdade deve estar entre 0 e 100");

        verify(companheiroRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao adicionar companheiro com lealdade acima de 100")
    void testAdicionarCompanheiro_LealdadeAcima100() {
        CompanheiroRequest companheiroRequest = new CompanheiroRequest();
        companheiroRequest.setNome("Arwen");
        companheiroRequest.setEspecie("ELFA");
        companheiroRequest.setLealdade(150);

        when(aventureiroRepository.findById(1L)).thenReturn(Optional.of(aventureiro));

        assertThatThrownBy(() -> aventureiroService.adicionarCompanheiro(1L, companheiroRequest))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Lealdade deve estar entre 0 e 100");

        verify(companheiroRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve buscar aventureiro por ID com sucesso")
    void testBuscarPorId_Sucesso() {
        when(aventureiroRepository.findById(1L)).thenReturn(Optional.of(aventureiro));

        AventureiroResponse response = aventureiroService.buscarPorId(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getNome()).isEqualTo("Aragorn");

        verify(aventureiroRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar aventureiro inexistente")
    void testBuscarPorId_NaoEncontrado() {
        when(aventureiroRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> aventureiroService.buscarPorId(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Aventureiro não encontrado");

        verify(aventureiroRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve listar aventureiros por organização")
    void testListarPorOrganizacao_Sucesso() {
        List<Aventureiro> aventureiros = new ArrayList<>();
        aventureiros.add(aventureiro);

        Aventureiro aventureiro2 = new Aventureiro();
        aventureiro2.setId(2L);
        aventureiro2.setNome("Legolas");
        aventureiro2.setClasse(ClasseEnum.ARQUEIRO);
        aventureiro2.setNivel(5);
        aventureiro2.setOrganizacao(organizacao);
        aventureiro2.setUsuarioCadastro(usuario);
        aventureiros.add(aventureiro2);

        when(organizacaoRepository.existsById(1L)).thenReturn(true);
        when(aventureiroRepository.findByOrganizacaoId(1L)).thenReturn(aventureiros);

        List<AventureiroResponse> responses = aventureiroService.listarPorOrganizacao(1L);

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getNome()).isEqualTo("Aragorn");
        assertThat(responses.get(1).getNome()).isEqualTo("Legolas");

        verify(aventureiroRepository).findByOrganizacaoId(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao listar aventureiros de organização inexistente")
    void testListarPorOrganizacao_OrganizacaoNaoEncontrada() {
        when(organizacaoRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> aventureiroService.listarPorOrganizacao(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Organização não encontrada");

        verify(aventureiroRepository, never()).findByOrganizacaoId(any());
    }
}
