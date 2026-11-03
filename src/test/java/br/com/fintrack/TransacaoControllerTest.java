package br.com.fintrack;

import br.com.fintrack.controller.TransacaoController;
import br.com.fintrack.dto.TransacaoResponseDTO;
import br.com.fintrack.enums.CategoriaTransacao;
import br.com.fintrack.enums.TipoTransacao;
import br.com.fintrack.exception.RecursoNaoEncontradoException;
import br.com.fintrack.model.Transacao;
import br.com.fintrack.service.TransacaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(TransacaoController.class)
class TransacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransacaoService transacaoService;

    @Test
    void deveRetornar200AoListarTransacoes() throws Exception {

        when(transacaoService.listar()).thenReturn(List.of());

        mockMvc.perform(get("/transacoes"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void deveRetornar201AoCadastrarTransacao() throws Exception {

        String json = """
            {
                "descricao": "Salário",
                "valor": 3000.00,
                "tipo": "RECEITA",
                "data": "2026-10-20",
                "categoria": "%s"
            }
            """.formatted(br.com.fintrack.enums.CategoriaTransacao.values()[0].name());

        br.com.fintrack.model.Transacao transacao =
                new br.com.fintrack.model.Transacao();

        transacao.setDescricao("Salário");
        transacao.setValor(new java.math.BigDecimal("3000.00"));
        transacao.setTipo(br.com.fintrack.enums.TipoTransacao.RECEITA);
        transacao.setData(java.time.LocalDate.of(2026, 10, 20));
        transacao.setCategoria(
                br.com.fintrack.enums.CategoriaTransacao.values()[0]
        );

        when(transacaoService.converteParaCadastro(any()))
                .thenReturn(transacao);

        when(transacaoService.converteParaResponse(transacao))
                .thenReturn(new br.com.fintrack.dto.TransacaoResponseDTO(
                        1L,
                        transacao.getDescricao(),
                        transacao.getValor(),
                        transacao.getTipo(),
                        transacao.getData(),
                        transacao.getCategoria()
                ));

        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descricao").value("Salário"));
    }

    @Test
    void deveRetornar400QuandoDescricaoestiverVazia() throws Exception {

        String json = """
                {
                    "descrição": "",
                    "valor": 3000.00,
                    "tipo": "RECEITA",
                    "data": "2026-10-20",
                    "categoria": "%s"
                }    
                """.formatted(
                br.com.fintrack.enums.CategoriaTransacao.values()[0].name()
        );

        mockMvc.perform(post("/transacoes")
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .content(json))
                .andExpect(status().isBadRequest());

    }

    @Test
    void deveRetornar400QuandoTransacaoNaoExistir() throws Exception {

        when(transacaoService.buscarObrigatorioPorId(999L))
                .thenThrow(
                        new RecursoNaoEncontradoException("Transação não encontrada com o ID: 999")
                );

        mockMvc.perform(get("/transacoes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornar200AoAtualizarTransacao() throws Exception {

        CategoriaTransacao categoria =
                CategoriaTransacao.values()[0];

        TransacaoResponseDTO resposta = new TransacaoResponseDTO(
                1L,
                "Salário atualizado",
                new BigDecimal("3500.00"),
                TipoTransacao.RECEITA,
                LocalDate.of(2026, 10, 20),
                categoria
        );

        when(transacaoService.atualizarComDTO(
                org.mockito.ArgumentMatchers.eq(1L),
                any(br.com.fintrack.dto.TransacaoRequestDTO.class)
        )).thenReturn(java.util.Optional.of(resposta));

        String json = """
            {
                "descricao": "Salário atualizado",
                "valor": 3500.00,
                "tipo": "RECEITA",
                "data": "2026-10-20",
                "categoria": "%s"
            }
            """.formatted(categoria.name());

        mockMvc.perform(put("/transacoes/1")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descricao").value("Salário atualizado"));
    }

    @Test
    void deveRetornar404AoAtualizarTransacaoExistente() throws Exception {

        CategoriaTransacao categoria = CategoriaTransacao.values()[0];

        when(transacaoService.atualizarComDTO(
                org.mockito.ArgumentMatchers.eq(999L),
                any(br.com.fintrack.dto.TransacaoRequestDTO.class)
        )).thenReturn(java.util.Optional.empty());

        String json = """
                {
                    "descricao": "Salário atualizado",
                    "valor": 3500.00,
                    "tipo": "RECEITA",
                    "data": "2026-10-20",
                    "categoria": "%s"
                }    
                """.formatted(categoria.name());

        mockMvc.perform(put("/transacoes/999")
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .content(json))
                .andExpect(status().isNotFound());

    }

    @Test
    void deveRetornar204AoExcluirTransacao() throws Exception {

        when(transacaoService.buscarObrigatorioPorId(1L))
                .thenReturn(new Transacao());

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .delete("/transacoes/1")
        )
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar404AoExcluirTransacaoInexxistente() throws Exception {

        when(transacaoService.buscarObrigatorioPorId(999L))
                .thenThrow(new RecursoNaoEncontradoException("Transação não encontrada"));

        mockMvc.perform(delete("/transacoes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornar400QuandoDataInicialForPosteriorADataFinal() throws Exception {

        mockMvc.perform(get("/transacoes/periodo")
                .param("inicio", "2026-10-31")
                .param("fim","2026-10-01"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400QuandoMesForInvalido() throws Exception {

        mockMvc.perform(get("/transacoes/resumo/mensal")
                .param("ano","2026")
                .param("mes", "13"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400QuandoPaginaForNegativa() throws Exception {

        mockMvc.perform(get("/transacoes/paginadas")
                .param("pagina", "-1")
                .param("tamanho", "5"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400QuandoTamanhoDaPaginaForZero() throws Exception {

        mockMvc.perform(get("/transacoes/paginadas")
                .param("pagina", "0")
                .param("tamanho", "0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar200AoFiltrarPorTipo() throws Exception {

        when(transacaoService.listarPorTipo(TipoTransacao.RECEITA))
                .thenReturn(List.of());

        mockMvc.perform(get("/transacoes")
                .param("tipo", "RECEITA"))
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornar400QuandoTipoForInvalido() throws Exception {

        mockMvc.perform(get("/transacoes")
                .param("tipo","INVALIDO"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400QuandoPeriodoDoResumoForInvalido() throws Exception {

        mockMvc.perform(get("/transacoes/resumo/periodo")
                .param("inicio", "2026-10-31")
                .param("fim", "2026-10-01"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400QuandoAnoDoRelatorioForInvalido() throws Exception {

        mockMvc.perform(get("/transacoes/resumo/mensal")
                .param("ano", "0")
                .param("mes", "10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar200AoConsultarRelatorioPorCategoria() throws Exception {

        when(transacaoService.somaPorCategoria(TipoTransacao.RECEITA))
                .thenReturn(List.of());

        mockMvc.perform(get("/transacoes/relatorio/categorias")
                .param("tipo", "DESPESA"))
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornar200AoConsultarRelatorioMensal() throws Exception {
        when(transacaoService.somaPorTipoNoPeriodo(
                TipoTransacao.RECEITA,
                java.time.LocalDate.of(2026, 10, 1),
                java.time.LocalDate.of(2026, 10, 31)
        )).thenReturn(java.math.BigDecimal.valueOf(5000));

        when(transacaoService.somaPorTipoNoPeriodo(
                TipoTransacao.DESPESA,
                java.time.LocalDate.of(2026, 10, 1),
                java.time.LocalDate.of(2026, 10, 31)
        )).thenReturn(java.math.BigDecimal.valueOf(2000));

        mockMvc.perform(get("/transacoes/resumo/mensal")
                        .param("ano", "2026")
                        .param("mes", "10"))
                .andExpect(status().isOk());
    }


    @Test
    void deveRetornar200AoConsultarResumoFinanceiro() throws Exception {

        when(transacaoService.somarPorTipo(TipoTransacao.RECEITA))
                .thenReturn(new BigDecimal("5000.00"));

        when(transacaoService.somarPorTipo(TipoTransacao.DESPESA))
                .thenReturn(new BigDecimal("2000.00"));

        mockMvc.perform(get("/transacoes/resumo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.receitas").value(5000.00))
                .andExpect(jsonPath("$.despesas").value(2000.00))
                .andExpect(jsonPath("$.saldo").value(3000.00));
    }

    @Test
    void deveRetornar200AoBuscarTransacaoPorId() throws Exception {

        Transacao transacao = new Transacao();

        transacao.setDescricao("Salário");
        transacao.setValor((new BigDecimal("5000.00")));
        transacao.setTipo(TipoTransacao.RECEITA);
        transacao.setData(LocalDate.of(2026,10,20));
        transacao.setCategoria(CategoriaTransacao.values()[0]);


        when(transacaoService.buscarObrigatorioPorId(1L))
                .thenReturn(transacao);


        mockMvc.perform(get("/transacoes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Salário"))
                .andExpect(jsonPath("$.valor").value(5000.00))
                .andExpect(jsonPath("$.tipo").value("RECEITA"))
                .andExpect(jsonPath("$.categoria").value("ALIMENTACAO"))
                .andExpect(jsonPath("$.data").value("2026-10-20"));

    }

    @Test
    void deveRetornar200AoListarTransacoesOrdenadasPorData() throws Exception {

        Transacao transacao = new Transacao();

        TransacaoResponseDTO respostarDTO = org.mockito.Mockito.mock(TransacaoResponseDTO.class);

        when(transacaoService.listarOrdenadasPorData())
                .thenReturn(List.of(transacao));

        when(transacaoService.converteParaResponse(transacao))
                .thenReturn(respostarDTO);

        mockMvc.perform(get("/transacoes/ordenadas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(transacaoService).listarOrdenadasPorData();
        verify(transacaoService).converteParaResponse(transacao);
    }

    @Test
    void deveRetornar200AoListarTransacoesPaginadas() throws Exception {

        Transacao transacao = new Transacao();

        TransacaoResponseDTO respostaDTO =
                org.mockito.Mockito.mock(TransacaoResponseDTO.class);

        org.springframework.data.domain.Pageable pageable =
                org.springframework.data.domain.PageRequest.of(0, 5);

        org.springframework.data.domain.Page<Transacao> pagina =
                new org.springframework.data.domain.PageImpl<>(
                        List.of(transacao), pageable, 1
                );

        when(transacaoService.listarPaginadas(pageable))
                .thenReturn(pagina);

        when(transacaoService.converteParaResponse(transacao))
                .thenReturn(respostaDTO);

        mockMvc.perform(get("/transacoes/paginadas")
                        .param("pagina", "0")
                        .param("tamanho", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1));

        verify(transacaoService).listarPaginadas(pageable);
        verify(transacaoService).converteParaResponse(transacao);
    }

    @Test
    void deveRetornar200AoListarTransacoesPorPeriodo() throws Exception {

        LocalDate inicio = LocalDate.of(2026, 10, 1);
        LocalDate fim = LocalDate.of(2026, 10, 31);

        Transacao transacao = new Transacao();

        TransacaoResponseDTO respostaDTO = org.mockito.Mockito.mock(TransacaoResponseDTO.class);

        when(transacaoService.listarPorPeriodo(inicio, fim))
                .thenReturn(List.of(transacao));

        when(transacaoService.converteParaResponse(transacao))
                .thenReturn(respostaDTO);

        mockMvc.perform(get("/transacoes/periodo")
                    .param("inicio", "2026-10-01")
                    .param("fim", "2026-10-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(transacaoService).listarPorPeriodo(inicio, fim);
        verify(transacaoService).converteParaResponse(transacao);
    }

    @Test
    void deveRetornar200AoConsultarResumoPorPeriodo() throws Exception {
        LocalDate inicio = LocalDate.of(2026,10,1);
        LocalDate fim = LocalDate.of(2026,10,31);

        when(transacaoService.somaPorTipoNoPeriodo(
                TipoTransacao.RECEITA, inicio, fim
        )).thenReturn(new BigDecimal("5000.00"));

        when(transacaoService.somaPorTipoNoPeriodo(
                TipoTransacao.DESPESA, inicio, fim
        )).thenReturn(new BigDecimal("2000.00"));

        mockMvc.perform(get("/transacoes/resumo/periodo")
                        .param("inicio", "2026-10-01")
                        .param("fim", "2026-10-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.receitas").value(5000.00))
                .andExpect(jsonPath("$.despesas").value(2000.00))
                .andExpect(jsonPath("$.saldo").value(3000.00));

    }


    @Test
    void deveRetornar400QuandoMesForZero() throws Exception {

        mockMvc.perform(get("/transacoes/resumo/mensal")
                        .param("ano", "2026")
                        .param("mes", "0"))
                .andExpect(status().isBadRequest());
    }

}