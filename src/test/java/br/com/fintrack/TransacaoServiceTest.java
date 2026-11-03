package br.com.fintrack;

import br.com.fintrack.dto.RelatorioCategoriaDTO;
import br.com.fintrack.dto.TransacaoRequestDTO;
import br.com.fintrack.dto.TransacaoResponseDTO;
import br.com.fintrack.enums.CategoriaTransacao;
import br.com.fintrack.enums.TipoTransacao;
import br.com.fintrack.exception.RecursoNaoEncontradoException;
import br.com.fintrack.model.Transacao;
import br.com.fintrack.repository.TransacaoRepository;
import br.com.fintrack.service.TransacaoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TransacaoServiceTest {

    @Mock
    private TransacaoRepository transacaoRepository;

    @InjectMocks
    private TransacaoService transacaoService;

    @Test
    void deveCriarServiceCorretamente() {
        assertNotNull(transacaoService);
    }

    @Test
    void deveListarTransacoes() {

        List<Transacao> transacaos = List.of(
                new Transacao(),
                new Transacao()
        );

        when(transacaoRepository.findAll()).thenReturn(transacaos);

        List<Transacao> resultado = transacaoService.listar();

        assertEquals(2, resultado.size());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistiremTransacoes() {
        when(transacaoRepository.findAll()).thenReturn(List.of());

        List<Transacao> resultado = transacaoService.listar();

        assertEquals(0, resultado.size());
    }

    @Test
    void deveBuscarTransacaoPorId() {
        Transacao transacao = new Transacao();

        when(transacaoRepository.findById(1L)).thenReturn(Optional.of(transacao));

        Optional<Transacao> resultado = transacaoService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
    }

    @Test
    void deveRetornarVazioQuandoIdNaoExistir() {

        when(transacaoRepository.findById(99l)).thenReturn(Optional.empty());

        Optional<Transacao> resultado = transacaoService.buscarPorId(99L);

        assertFalse(resultado.isPresent());
    }

    @Test
    void deveSalvarTransacao() {
        Transacao transacao = new Transacao();

        transacaoService.salvar(transacao);

        verify(transacaoRepository).save(transacao);
    }

    @Test
    void deveAtualizarTransacaoExistente() {

        Transacao existente = new Transacao();
        Transacao dadosAtualizados = new Transacao();

        dadosAtualizados.setDescricao("Mercado");

        when(transacaoRepository.findById(1L)).thenReturn(Optional.of(existente));

        when(transacaoRepository.save(existente)).thenReturn(existente);

        Optional<Transacao> resultado = transacaoService.atualizar(1L, dadosAtualizados);

        assertTrue(resultado.isPresent());

        assertEquals("Mercado", existente.getDescricao());

        verify(transacaoRepository).save(existente);
    }

    @Test
    void naoDeveAtualizarTransacaoExistente() {
        Transacao dadosAtualizados = new Transacao();

        when(transacaoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Transacao> resultado = transacaoService.atualizar(99L, dadosAtualizados);

        assertTrue(resultado.isEmpty());

        verify(transacaoRepository, never()).save(any(Transacao.class));
    }

    @Test
    void deveExcluirTransacao() {
        transacaoService.excluir(1L);

        verify(transacaoRepository).deleteById(1L);
    }

    @Test
    void deveLancarExcesaoQuandoTransacaoNaoExistir() {

        when(transacaoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> transacaoService.buscarObrigatorioPorId(99L));
    }

    @Test
    void deveSomarTransacoesPorTipo() {

        TipoTransacao tipo = TipoTransacao.RECEITA;

        when(transacaoRepository.somaPorTipo(tipo)).thenReturn(new BigDecimal("1500.00"));

        BigDecimal resultado = transacaoService.somarPorTipo(tipo);

        assertEquals(new BigDecimal("1500.00"),resultado);

        verify(transacaoRepository).somaPorTipo(tipo);
    }

    @Test
    void deveSomarTransacoesPorTipoNoPeriodo() {
        TipoTransacao tipo = TipoTransacao.RECEITA;

        LocalDate inicio = LocalDate.of(2026,10,1);
        LocalDate fim = LocalDate.of(2026,10,31);

        BigDecimal valorEsperado = new BigDecimal("2500.00");

        when(transacaoRepository.somaPorTipoNoPeriodo(tipo,inicio,fim)).thenReturn(valorEsperado);

        BigDecimal resultado = transacaoService.somaPorTipoNoPeriodo(tipo, inicio, fim);

        assertEquals(valorEsperado, resultado);

        verify(transacaoRepository).somaPorTipoNoPeriodo(tipo,inicio,fim);
    }

    @Test
    void deveGerarRelatorioPorCategoria() {

        TipoTransacao tipo = TipoTransacao.RECEITA;
        CategoriaTransacao categoria = CategoriaTransacao.values()[0];

        Object[] linha =
                { categoria, new BigDecimal("500.00")
        };

        when(transacaoRepository.somarPorCategoria(tipo)).thenReturn(List.<Object[]>of(linha));

        List<RelatorioCategoriaDTO> resultado = transacaoService.somaPorCategoria(tipo);

        assertEquals(1, resultado.size());

        verify(transacaoRepository).somarPorCategoria(tipo);
    }

    @Test
    void deveListarTransacoesPorTipo() {
        TipoTransacao tipo = TipoTransacao.RECEITA;
        List<Transacao> transacaos = List.of(new Transacao());

        when(transacaoRepository.findByTipo(tipo)).thenReturn(transacaos);

        List<Transacao> resultado = transacaoService.listarPorTipo(tipo);

        assertEquals(1, resultado.size());
        verify(transacaoRepository).findByTipo(tipo);
    }

    @Test
    void deveListarTransacoesPorPeriodo() {
        LocalDate inicio = LocalDate.of(2026,10,1);
        LocalDate fim = LocalDate.of(2026,10,31);

        List<Transacao> transacoes = List.of(new Transacao());

        when(transacaoRepository.findByDataBetween(inicio, fim)).thenReturn(transacoes);

        List<Transacao> resultado = transacaoService.listarPorPeriodo(inicio, fim);

        assertEquals(1, resultado.size());

        verify(transacaoRepository).findByDataBetween(inicio, fim);
    }

    @Test
    void deveListarTransacoesOrdenadasPorData() {
        List<Transacao> transacaos = List.of(
                new Transacao(),
                new Transacao()
        );

        when(transacaoRepository.findAllByOrderByDataDesc()).thenReturn(transacaos);

        List<Transacao> resultado = transacaoService.listarOrdenadasPorData();

        assertEquals(2, resultado.size());

        verify(transacaoRepository).findAllByOrderByDataDesc();
    }

    @Test
    void deveListarTransacoesPaginadas() {
        PageRequest pageable = PageRequest.of(0, 5);

        List<Transacao> transacaos = List.of(
                new Transacao(),
                new Transacao()
        );

        Page<Transacao> paginaEsperada = new PageImpl<>(transacaos, pageable, transacaos.size());

        when(transacaoRepository.findAll(pageable)).thenReturn(paginaEsperada);

        Page<Transacao> resultado = transacaoService.listarPaginadas(pageable);

        assertEquals(2, resultado.getTotalElements());
        assertEquals(5,resultado.getSize());
        assertEquals(0,resultado.getNumber());

        verify(transacaoRepository).findAll(pageable);
    }

    @Test
    void deveConverterTransacaoParaResponseDTO() throws Exception {

        Transacao transacao = new Transacao();

        transacao.setId(1L);
        transacao.setDescricao("Salário");
        transacao.setValor(new BigDecimal("5000.00"));
        transacao.setTipo(TipoTransacao.RECEITA);
        transacao.setData(LocalDate.of(2026,10,20));
        transacao.setCategoria(CategoriaTransacao.ALIMENTACAO);


        TransacaoResponseDTO resposta = transacaoService.converteParaResponse(transacao);

        assertNotNull(resposta);
        assertEquals(1L,resposta.getId());
        assertEquals("Salário",resposta.getDescricao());
        assertEquals(new BigDecimal("5000.00"),resposta.getValor());
        assertEquals(TipoTransacao.RECEITA, resposta.getTipo());
        assertEquals(LocalDate.of(2026,10,20),resposta.getData());
        assertEquals(CategoriaTransacao.ALIMENTACAO,resposta.getCategoria());
    }

    @Test
    void deveConverterRequestDTOParaCadastro() throws Exception {

        TransacaoRequestDTO dto = new TransacaoRequestDTO();

        dto.setDescricao("Salário");
        dto.setValor(new BigDecimal("5000.00"));
        dto.setTipo(TipoTransacao.RECEITA);
        dto.setData(LocalDate.of(2026,10,20));
        dto.setCategoria(CategoriaTransacao.ALIMENTACAO);

        Transacao transacaoSalva = new Transacao();

        transacaoSalva.setId(1L);
        transacaoSalva.setDescricao(dto.getDescricao());
        transacaoSalva.setValor(dto.getValor());
        transacaoSalva.setTipo(dto.getTipo());
        transacaoSalva.setData(dto.getData());
        transacaoSalva.setCategoria(dto.getCategoria());

        when(transacaoRepository.save(any(Transacao.class)))
                .thenReturn(transacaoSalva);

        Transacao resultado = transacaoService.converteParaCadastro(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Salário", resultado.getDescricao());
        assertEquals(new BigDecimal("5000.00"), resultado.getValor());
        assertEquals(TipoTransacao.RECEITA, resultado.getTipo());

        verify(transacaoRepository).save(any(Transacao.class));
    }

    @Test
    void deveAtualizarTransacaoComDTO() {

        Transacao existente = new Transacao();
        existente.setId(1L);
        existente.setDescricao("Compra Antiga");

        TransacaoRequestDTO dto = new TransacaoRequestDTO();
        dto.setDescricao("Compra Atualizada");
        dto.setValor(new BigDecimal("250.00"));
        dto.setTipo(TipoTransacao.DESPESA);
        dto.setData(LocalDate.of(2026,10,20));
        dto.setCategoria(CategoriaTransacao.ALIMENTACAO);


        when(transacaoRepository.findById(1L))
                .thenReturn(Optional.of(existente));

        when(transacaoRepository.save(any(Transacao.class)))
                .thenReturn(existente);

        Optional<TransacaoResponseDTO> resultado = transacaoService.atualizarComDTO(1L, dto);

        assertTrue(resultado.isPresent());
        assertEquals("Compra Atualizada", resultado.get().getDescricao());
        assertEquals(new BigDecimal("250.00"), resultado.get().getValor());
        assertEquals(TipoTransacao.DESPESA,resultado.get().getTipo());

        verify(transacaoRepository).save(existente);
    }

    @Test
    void naoDeveAtualizarTransacaoComDTOQuandoIdNaoExistir() {

        TransacaoRequestDTO dto = new TransacaoRequestDTO();
        dto.setDescricao("Compra Atualizada");
        dto.setValor(new BigDecimal("250.00"));
        dto.setTipo(TipoTransacao.DESPESA);
        dto.setData(LocalDate.of(2026, 10, 20));
        dto.setCategoria(CategoriaTransacao.ALIMENTACAO);

        when(transacaoRepository.findById(99L))
                .thenReturn(Optional.empty());

        Optional<TransacaoResponseDTO> resultado =
                transacaoService.atualizarComDTO(99L, dto);

        assertTrue(resultado.isEmpty());

        verify(transacaoRepository, never()).save(any(Transacao.class));
    }

    @Test
    void deveBuscarObrigatoriamenteTransacaoExistente() {

        Transacao transacao = new Transacao();
        transacao.setId(1L);
        transacao.setDescricao("Mercado");

        when(transacaoRepository.findById(1L))
                .thenReturn(Optional.of(transacao));

        Transacao resultado = transacaoService.buscarObrigatorioPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Mercado", resultado.getDescricao());

        verify(transacaoRepository).findById(1L);
    }
}