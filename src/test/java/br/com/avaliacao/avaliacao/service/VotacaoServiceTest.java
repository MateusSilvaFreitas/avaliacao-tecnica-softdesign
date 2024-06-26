package br.com.avaliacao.avaliacao.service;

import br.com.avaliacao.avaliacao.exception.PautaFinalizadaException;
import br.com.avaliacao.avaliacao.exception.PautaJaIniciadaException;
import br.com.avaliacao.avaliacao.exception.PautaNaoExistenteException;
import br.com.avaliacao.avaliacao.exception.VotoRealizadoException;
import br.com.avaliacao.avaliacao.model.Associado;
import br.com.avaliacao.avaliacao.model.ETipoVoto;
import br.com.avaliacao.avaliacao.model.Pauta;
import br.com.avaliacao.avaliacao.model.Voto;
import br.com.avaliacao.avaliacao.model.dto.AbrirPautaDTO;
import br.com.avaliacao.avaliacao.model.dto.CriarPautaDTO;
import br.com.avaliacao.avaliacao.model.dto.PautaDTO;
import br.com.avaliacao.avaliacao.model.dto.VotoDTO;
import br.com.avaliacao.avaliacao.repository.associado.AssociadoRepository;
import br.com.avaliacao.avaliacao.repository.pauta.PautaRepository;
import br.com.avaliacao.avaliacao.repository.voto.VotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class VotacaoServiceTest {

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private AssociadoRepository associadoRepository;

    @InjectMocks
    private VotacaoService votacaoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCriarPauta() {
        CriarPautaDTO dto = new CriarPautaDTO("Investir R$20.000 em programas escolares");

        Pauta pautaSalva = new Pauta("Investir R$20.000 em programas escolares");
        when(pautaRepository.save(any(Pauta.class))).thenReturn(pautaSalva);

        PautaDTO result = votacaoService.criarPauta(dto);

        assertEquals("Investir R$20.000 em programas escolares", result.getNome());
    }

    @Test
    public void testAbrirPauta() {
        AbrirPautaDTO dto = new AbrirPautaDTO(10, 1L);

        Pauta pautaExistente = new Pauta("Investir R$20.000 em programas escolares");
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pautaExistente));
        when(pautaRepository.save(any(Pauta.class))).thenReturn(pautaExistente);

        votacaoService.abrirPauta(dto);

        assertNotNull(pautaExistente.getDataMaximaVotacao());
    }

    @Test
    public void testVotar() {
        VotoDTO dto = new VotoDTO(1L, 1L, ETipoVoto.SIM);

        Associado associado = new Associado();
        associado.setId(1L);
        when(associadoRepository.findById(1L)).thenReturn(Optional.of(associado));

        when(votoRepository.findByIdPautaAndIdAssociado(anyLong(), anyLong())).thenReturn(null);

        Pauta pauta = new Pauta();
        pauta.setId(1L);
        when(pautaRepository.verificarDataMaximaPauta(1L)).thenReturn(pauta);

        Voto votoSalvo = new Voto();
        votoSalvo.setId(1L);
        when(votoRepository.save(any(Voto.class))).thenReturn(votoSalvo);

        assertDoesNotThrow(() -> votacaoService.votar(dto));
    }

    @Test
    public void testContabilizarPauta() {
        Pauta pauta = new Pauta();
        pauta.setId(1L);
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));

        Object[] sim = { ETipoVoto.SIM, 5L };
        Object[] nao = { ETipoVoto.NAO, 3L };
        List<Object[]> resultados = new ArrayList<>();
        resultados.add(sim);
        resultados.add(nao);
        when(pautaRepository.contarVotosPorPauta(1L)).thenReturn(resultados);

        PautaDTO result = votacaoService.contabilizarPauta(1L);

        assertEquals(5L, result.getVotosSim());
        assertEquals(3L, result.getVotosNao());
    }

    @Test
    public void testCriarPauta_PautaNaoExistenteException() {
        CriarPautaDTO dto = new CriarPautaDTO("Investir R$20.000 em programas escolares");

        when(pautaRepository.save(any(Pauta.class))).thenThrow(PautaNaoExistenteException.class);

        assertThrows(PautaNaoExistenteException.class, () -> votacaoService.criarPauta(dto));
    }

    @Test
    public void testAbrirPauta_PautaNaoExistenteException() {
        AbrirPautaDTO dto = new AbrirPautaDTO(10, 1L);

        when(pautaRepository.findById(anyLong())).thenReturn(Optional.empty());

        PautaNaoExistenteException exception = assertThrows(PautaNaoExistenteException.class, () -> votacaoService.abrirPauta(dto));
        assertEquals("Não existe pauta com id 1", exception.getMessage());
    }

    @Test
    public void testAbrirPauta_PautaJaIniciadaException() {
        AbrirPautaDTO dto = new AbrirPautaDTO(10, 1L);
        Pauta pautaExistente = new Pauta("Investir R$20.000 em programas escolares");
        pautaExistente.setId(0L);
        pautaExistente.iniciarVotacao(10); // Simula a pauta já iniciada
        when(pautaRepository.findById(anyLong())).thenReturn(Optional.of(pautaExistente));

        PautaJaIniciadaException exception = assertThrows(PautaJaIniciadaException.class, () -> votacaoService.abrirPauta(dto));
        assertEquals("A pauta 0 - Investir R$20.000 em programas escolares já foi iniciada", exception.getMessage());
    }

    @Test
    public void testVotar_VotoRealizadoException() {
        VotoDTO dto = new VotoDTO(1L, 1L, ETipoVoto.SIM);

        Associado associado = new Associado();
        associado.setId(1L);
        associado.setCpf("123");
        when(associadoRepository.findById(anyLong())).thenReturn(Optional.of(associado));

        when(votoRepository.findByIdPautaAndIdAssociado(anyLong(), anyLong())).thenReturn(new Voto());

        VotoRealizadoException exception = assertThrows(VotoRealizadoException.class, () -> votacaoService.votar(dto));
        assertEquals("O cpf 123 já realizou um voto nessa pauta", exception.getMessage()); // Aqui ajuste conforme a mensagem real
    }

    @Test
    public void testVotar_PautaFinalizadaException() {
        VotoDTO dto = new VotoDTO(1L, 1L, ETipoVoto.SIM);

        Associado associado = new Associado();
        associado.setId(1L);
        when(associadoRepository.findById(anyLong())).thenReturn(Optional.of(associado));

        when(votoRepository.findByIdPautaAndIdAssociado(anyLong(), anyLong())).thenReturn(null);
        when(pautaRepository.verificarDataMaximaPauta(anyLong())).thenReturn(null);

        PautaFinalizadaException exception = assertThrows(PautaFinalizadaException.class, () -> votacaoService.votar(dto));
        assertEquals("A pauta não está aberta para votação ou não existe", exception.getMessage());
    }

}