package com.guilda.registro.aventura.service;

import com.guilda.registro.aventura.controller.MissaoResponse;
import com.guilda.registro.aventura.controller.ParticipacaoResponse;
import com.guilda.registro.aventura.dto.MissaoCreateRequest;
import com.guilda.registro.aventura.dto.ParticipacaoRequest;
import com.guilda.registro.aventura.model.*;
import com.guilda.registro.aventura.repository.AventureiroRepository;
import com.guilda.registro.aventura.repository.MissaoRepository;
import com.guilda.registro.aventura.repository.ParticipacaoMissaoRepository;
import com.guilda.registro.audit.model.Organizacao;
import com.guilda.registro.audit.model.Usuario;
import com.guilda.registro.audit.repository.OrganizacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do MissaoService")
class MissaoServiceTest {

    @Mock
    private MissaoRepository missaoRepository;

    @Mock
    private OrganizacaoRepository organizacaoRepository;

    @Mock
    private AventureiroRepository aventureiroRepository;

    @Mock
    private ParticipacaoMissaoRepository participacaoRepository;

    @InjectMocks
    private MissaoService missaoService;

    private Organizacao organizacao;
    private Missao missao;
    private Aventureiro aventureiro;
    private Usuario usuario;
    private MissaoCreateRequest createRequest;

    // Obtém valores dinâmicos dos enums
    private final ClasseEnum classeQualquer = ClasseEnum.values()[0];
    private final NivelPerigoEnum nivelQualquer = NivelPerigoEnum.values()[0];
    private final StatusMissaoEnum statusPlanejada = StatusMissaoEnum.values()[0]; // assume que o primeiro é PLANEJADA
    private final StatusMissaoEnum statusAndamento = StatusMissaoEnum.values()[1 % StatusMissaoEnum.values().length];
    private final StatusMissaoEnum statusConcluida = StatusMissaoEnum.values()[2 % StatusMissaoEnum.values().length];
    private final StatusMissaoEnum statusCancelada = StatusMissaoEnum.values()[3 % StatusMissaoEnum.values().length];
    private final PapelMissaoEnum papelQualquer = PapelMissaoEnum.values()[0];
    private final String nomePapel = papelQualquer.name();

    @BeforeEach
    void setUp() {
        organizacao = new Organizacao();
        organizacao.setId(1L);
        organizacao.setNome("Guilda Aventureira");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Admin");

        aventureiro = new Aventureiro();
        aventureiro.setId(1L);
        aventureiro.setNome("Aragorn");
        aventureiro.setClasse(classeQualquer);
        aventureiro.setNivel(5);
        aventureiro.setAtivo(true);
        aventureiro.setOrganizacao(organizacao);
        aventureiro.setUsuarioCadastro(usuario);

        missao = new Missao();
        missao.setId(1L);
        missao.setTitulo("Derrotar o Dragão");
        missao.setNivelPerigo(nivelQualquer);
        missao.setStatus(statusPlanejada);
        missao.setOrganizacao(organizacao);
        missao.setDataInicio(LocalDate.now());
        missao.setDataTermino(LocalDate.now().plusDays(7));
        missao.setCreatedAt(OffsetDateTime.now());

        createRequest = new MissaoCreateRequest();
        createRequest.setOrganizacaoId(1L);
        createRequest.setTitulo("Derrotar o Dragão");
        createRequest.setNivelPerigo(nivelQualquer.name());
        createRequest.setDataInicio(LocalDate.now());
        createRequest.setDataTermino(LocalDate.now().plusDays(7));
    }

    @Test
    @DisplayName("Deve criar uma missão com sucesso")
    void testCriarMissao_Sucesso() {
        when(organizacaoRepository.findById(1L)).thenReturn(Optional.of(organizacao));
        when(missaoRepository.save(any(Missao.class))).thenReturn(missao);

        MissaoResponse response = missaoService.criarMissao(createRequest);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitulo()).isEqualTo("Derrotar o Dragão");
        assertThat(response.getNivelPerigo()).isEqualTo(nivelQualquer);
        assertThat(response.getStatus()).isEqualTo(statusPlanejada);

        verify(organizacaoRepository).findById(1L);
        verify(missaoRepository).save(any(Missao.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar missão com organização inexistente")
    void testCriarMissao_OrganizacaoNaoEncontrada() {
        when(organizacaoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> missaoService.criarMissao(createRequest))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Organização não encontrada");

        verify(missaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve inscrever aventureiro em missão com sucesso")
    void testInscreverAventureiro_Sucesso() {
        ParticipacaoRequest participacaoRequest = new ParticipacaoRequest();
        participacaoRequest.setMissaoId(1L);
        participacaoRequest.setAventureiroId(1L);
        participacaoRequest.setPapel(nomePapel);
        participacaoRequest.setRecompensaOuro(1000);
        participacaoRequest.setDestaque(false);

        ParticipacaoMissao participacao = new ParticipacaoMissao();
        participacao.setId(1L);
        participacao.setMissao(missao);
        participacao.setAventureiro(aventureiro);
        participacao.setPapel(papelQualquer);
        participacao.setRecompensaOuro(1000);
        participacao.setDestaque(false);

        when(missaoRepository.findById(1L)).thenReturn(Optional.of(missao));
        when(aventureiroRepository.findById(1L)).thenReturn(Optional.of(aventureiro));
        when(participacaoRepository.existsByMissaoIdAndAventureiroId(1L, 1L)).thenReturn(false);
        when(participacaoRepository.save(any(ParticipacaoMissao.class))).thenReturn(participacao);

        ParticipacaoResponse response = missaoService.inscreverAventureiro(participacaoRequest);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getPapel()).isEqualTo(papelQualquer);
        assertThat(response.getRecompensaOuro()).isEqualTo(1000);

        verify(missaoRepository).findById(1L);
        verify(aventureiroRepository).findById(1L);
        verify(participacaoRepository).save(any(ParticipacaoMissao.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao inscrever aventureiro em missão inexistente")
    void testInscreverAventureiro_MissaoNaoEncontrada() {
        ParticipacaoRequest participacaoRequest = new ParticipacaoRequest();
        participacaoRequest.setMissaoId(1L);
        participacaoRequest.setAventureiroId(1L);
        participacaoRequest.setPapel(nomePapel);

        when(missaoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> missaoService.inscreverAventureiro(participacaoRequest))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Missão não encontrada");

        verify(participacaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao inscrever aventureiro inexistente")
    void testInscreverAventureiro_AventureiroNaoEncontrado() {
        ParticipacaoRequest participacaoRequest = new ParticipacaoRequest();
        participacaoRequest.setMissaoId(1L);
        participacaoRequest.setAventureiroId(1L);
        participacaoRequest.setPapel(nomePapel);

        when(missaoRepository.findById(1L)).thenReturn(Optional.of(missao));
        when(aventureiroRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> missaoService.inscreverAventureiro(participacaoRequest))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Aventureiro não encontrado");

        verify(participacaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao inscrever aventureiro inativo")
    void testInscreverAventureiro_AventureiroInativo() {
        aventureiro.setAtivo(false);
        ParticipacaoRequest participacaoRequest = new ParticipacaoRequest();
        participacaoRequest.setMissaoId(1L);
        participacaoRequest.setAventureiroId(1L);
        participacaoRequest.setPapel(nomePapel);

        when(missaoRepository.findById(1L)).thenReturn(Optional.of(missao));
        when(aventureiroRepository.findById(1L)).thenReturn(Optional.of(aventureiro));

        assertThatThrownBy(() -> missaoService.inscreverAventureiro(participacaoRequest))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Aventureiro inativo não pode participar de missões");

        verify(participacaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao inscrever aventureiro em missão concluída")
    void testInscreverAventureiro_MissaoConcluida() {
        missao.setStatus(statusConcluida);
        ParticipacaoRequest participacaoRequest = new ParticipacaoRequest();
        participacaoRequest.setMissaoId(1L);
        participacaoRequest.setAventureiroId(1L);
        participacaoRequest.setPapel(nomePapel);

        when(missaoRepository.findById(1L)).thenReturn(Optional.of(missao));
        when(aventureiroRepository.findById(1L)).thenReturn(Optional.of(aventureiro));

        assertThatThrownBy(() -> missaoService.inscreverAventureiro(participacaoRequest))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Missão não aceita novos participantes");

        verify(participacaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao inscrever aventureiro que já está inscrito")
    void testInscreverAventureiro_JaInscrito() {
        ParticipacaoRequest participacaoRequest = new ParticipacaoRequest();
        participacaoRequest.setMissaoId(1L);
        participacaoRequest.setAventureiroId(1L);
        participacaoRequest.setPapel(nomePapel);

        when(missaoRepository.findById(1L)).thenReturn(Optional.of(missao));
        when(aventureiroRepository.findById(1L)).thenReturn(Optional.of(aventureiro));
        when(participacaoRepository.existsByMissaoIdAndAventureiroId(1L, 1L)).thenReturn(true);

        assertThatThrownBy(() -> missaoService.inscreverAventureiro(participacaoRequest))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Aventureiro já está inscrito nesta missão");

        verify(participacaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao inscrever aventureiro de outra organização")
    void testInscreverAventureiro_OutraOrganizacao() {
        Organizacao outroOrg = new Organizacao();
        outroOrg.setId(2L);
        aventureiro.setOrganizacao(outroOrg);

        ParticipacaoRequest participacaoRequest = new ParticipacaoRequest();
        participacaoRequest.setMissaoId(1L);
        participacaoRequest.setAventureiroId(1L);
        participacaoRequest.setPapel(nomePapel);

        when(missaoRepository.findById(1L)).thenReturn(Optional.of(missao));
        when(aventureiroRepository.findById(1L)).thenReturn(Optional.of(aventureiro));
        when(participacaoRepository.existsByMissaoIdAndAventureiroId(1L, 1L)).thenReturn(false);

        assertThatThrownBy(() -> missaoService.inscreverAventureiro(participacaoRequest))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Aventureiro pertence a outra organização");

        verify(participacaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve listar missões por organização")
    void testListarPorOrganizacao_Sucesso() {
        List<Missao> missoes = new ArrayList<>();
        missoes.add(missao);

        when(missaoRepository.findByOrganizacaoId(1L)).thenReturn(missoes);

        List<MissaoResponse> responses = missaoService.listarPorOrganizacao(1L);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getTitulo()).isEqualTo("Derrotar o Dragão");

        verify(missaoRepository).findByOrganizacaoId(1L);
    }

    @Test
    @DisplayName("Deve iniciar uma missão planejada")
    void testIniciarMissao_Sucesso() {
        Missao missaoAtualizada = new Missao();
        missaoAtualizada.setId(1L);
        missaoAtualizada.setTitulo("Derrotar o Dragão");
        missaoAtualizada.setStatus(statusAndamento);
        missaoAtualizada.setOrganizacao(organizacao);

        when(missaoRepository.findById(1L)).thenReturn(Optional.of(missao));
        when(missaoRepository.save(any(Missao.class))).thenReturn(missaoAtualizada);

        MissaoResponse response = missaoService.iniciarMissao(1L);

        assertThat(response.getStatus()).isEqualTo(statusAndamento);

        verify(missaoRepository).findById(1L);
        verify(missaoRepository).save(any(Missao.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao iniciar missão não planejada")
    void testIniciarMissao_StatusInvalido() {
        missao.setStatus(statusConcluida);

        when(missaoRepository.findById(1L)).thenReturn(Optional.of(missao));

        assertThatThrownBy(() -> missaoService.iniciarMissao(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Apenas missões em status PLANEJADA podem ser iniciadas");

        verify(missaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve concluir uma missão em andamento")
    void testConcluirMissao_Sucesso() {
        missao.setStatus(statusAndamento);
        Missao missaoAtualizada = new Missao();
        missaoAtualizada.setId(1L);
        missaoAtualizada.setStatus(statusConcluida);
        missaoAtualizada.setOrganizacao(organizacao);

        when(missaoRepository.findById(1L)).thenReturn(Optional.of(missao));
        when(missaoRepository.save(any(Missao.class))).thenReturn(missaoAtualizada);

        MissaoResponse response = missaoService.concluirMissao(1L);

        assertThat(response.getStatus()).isEqualTo(statusConcluida);

        verify(missaoRepository).findById(1L);
        verify(missaoRepository).save(any(Missao.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao concluir missão não em andamento")
    void testConcluirMissao_StatusInvalido() {
        missao.setStatus(statusPlanejada);

        when(missaoRepository.findById(1L)).thenReturn(Optional.of(missao));

        assertThatThrownBy(() -> missaoService.concluirMissao(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Apenas missões em status EM_ANDAMENTO podem ser concluídas");

        verify(missaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve cancelar uma missão planejada")
    void testCancelarMissao_Sucesso() {
        Missao missaoCancelada = new Missao();
        missaoCancelada.setId(1L);
        missaoCancelada.setStatus(statusCancelada);
        missaoCancelada.setOrganizacao(organizacao);

        when(missaoRepository.findById(1L)).thenReturn(Optional.of(missao));
        when(missaoRepository.save(any(Missao.class))).thenReturn(missaoCancelada);

        MissaoResponse response = missaoService.cancelarMissao(1L);

        assertThat(response.getStatus()).isEqualTo(statusCancelada);

        verify(missaoRepository).findById(1L);
        verify(missaoRepository).save(any(Missao.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao cancelar missão já concluída")
    void testCancelarMissao_MissaoConcluida() {
        missao.setStatus(statusConcluida);

        when(missaoRepository.findById(1L)).thenReturn(Optional.of(missao));

        assertThatThrownBy(() -> missaoService.cancelarMissao(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Missões concluídas ou canceladas não podem ser alteradas");

        verify(missaoRepository, never()).save(any());
    }
}