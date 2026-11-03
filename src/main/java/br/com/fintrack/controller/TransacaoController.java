package br.com.fintrack.controller;

import br.com.fintrack.dto.*;
import br.com.fintrack.enums.TipoTransacao;
import br.com.fintrack.model.Transacao;
import br.com.fintrack.service.TransacaoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;


@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<Transacao> buscarPorId(@PathVariable Long id) {
        Transacao transacao = transacaoService.buscarObrigatorioPorId(id);
        return ResponseEntity.ok(transacao);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        transacaoService.buscarObrigatorioPorId(id);

        transacaoService.excluir(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransacaoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody TransacaoRequestDTO dto
    ) {
        return transacaoService.atualizarComDTO(id, dto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<TransacaoResponseDTO>> listarTodos(@RequestParam(required = false)TipoTransacao tipo) {

        List<Transacao> transacaos;

        if (tipo == null) {
            transacaos = transacaoService.listar();
        } else {
            transacaos = transacaoService.listarPorTipo(tipo);
        }

        List<TransacaoResponseDTO> resposta = transacaos.stream()
                .map(transacaoService::converteParaResponse)
                .toList();

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/resumo")
    public ResponseEntity<ResumoFinanceiroDTO> resumo() {

        BigDecimal receitas = transacaoService.somarPorTipo(TipoTransacao.RECEITA);

        BigDecimal despesas = transacaoService.somarPorTipo(TipoTransacao.DESPESA);

        BigDecimal saldo = receitas.subtract(despesas);

        ResumoFinanceiroDTO resumo = new ResumoFinanceiroDTO(receitas, despesas, saldo);

        return ResponseEntity.ok(resumo);
    }

    @GetMapping("/resumo/periodo")
    public ResponseEntity<ResumoPorPeriodoDTO> resumoPorPeriodo(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim
    ) {

        if (inicio.isAfter(fim)) {
            return ResponseEntity.badRequest().build();
        }

        BigDecimal receitas = transacaoService.somaPorTipoNoPeriodo(TipoTransacao.RECEITA, inicio, fim);

        BigDecimal despesas = transacaoService.somaPorTipoNoPeriodo(TipoTransacao.DESPESA, inicio, fim);

        BigDecimal saldo = receitas.subtract(despesas);

        ResumoPorPeriodoDTO resposta = new ResumoPorPeriodoDTO(inicio, fim, receitas, despesas, saldo);

        return ResponseEntity.ok(resposta);
    }

    @Operation(
            summary = "Consultar resumo financeiro mensal",
            description = "Retornar receitas, despesas e saldo de um mês específico"
    )
    @GetMapping("/resumo/mensal")
    public ResponseEntity<RelatorioMensalDT0> resumoMensal(
            @RequestParam int ano,
            @RequestParam int mes
    ) {
        if (ano < 1 || mes < 1 || mes > 12) {
            return ResponseEntity.badRequest().build();
        }

        LocalDate inicio = LocalDate.of(ano,mes, 1);
        LocalDate fim = inicio.withDayOfMonth(inicio.lengthOfMonth());

        BigDecimal receitas = transacaoService.somaPorTipoNoPeriodo(TipoTransacao.RECEITA, inicio, fim);

        BigDecimal despesas = transacaoService.somaPorTipoNoPeriodo(TipoTransacao.DESPESA, inicio, fim);

        BigDecimal saldo = receitas.subtract(despesas);

        RelatorioMensalDT0 resposta = new RelatorioMensalDT0(ano, mes, receitas, despesas, saldo);

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/relatorio/categorias")
    public ResponseEntity<List<RelatorioCategoriaDTO>> relatorioPorCategoria(
            @RequestParam TipoTransacao tipo
    ) {
        return ResponseEntity.ok(transacaoService.somaPorCategoria(tipo));
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<TransacaoResponseDTO>> listarPorPeriodo(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim
    ) {
        if (inicio.isAfter(fim)) {
            return ResponseEntity.badRequest().build();
        }

        List<TransacaoResponseDTO> reposta = transacaoService.listarPorPeriodo(inicio, fim)
                .stream()
                .map(transacaoService::converteParaResponse)
                .toList();

        return ResponseEntity.ok(reposta);
    }

    @GetMapping("/ordenadas")
    public ResponseEntity<List<TransacaoResponseDTO>> listarOdenadasPorData() {

        List<TransacaoResponseDTO> resposta = transacaoService.listarOrdenadasPorData()
                .stream()
                .map(transacaoService::converteParaResponse)
                .toList();

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/paginadas")
    public ResponseEntity<?> listarPaginadas(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "5") int tamanho) {

        if (pagina < 0) {
            return ResponseEntity.badRequest().body("O número de pagínas não pode ser negativo.");
        }

        if (tamanho <= 0) {
            return ResponseEntity.badRequest().body("O tamanho de página deve ser maior que zero.");
        }


        Pageable pageable = PageRequest.of(pagina, tamanho);

        Page<TransacaoResponseDTO> resposta = transacaoService.listarPaginadas(pageable)
                .map(transacaoService::converteParaResponse);

        return ResponseEntity.ok(resposta);
    }


    @PostMapping
    public ResponseEntity<TransacaoResponseDTO> criar(
            @Valid @RequestBody TransacaoRequestDTO dto
    ) {
        Transacao transacao = transacaoService.converteParaCadastro(dto);

        return ResponseEntity.status(201)
                .body(transacaoService.converteParaResponse(transacao));
    }

}