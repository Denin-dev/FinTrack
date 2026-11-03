package br.com.fintrack.service;

import br.com.fintrack.dto.RelatorioCategoriaDTO;
import br.com.fintrack.dto.TransacaoRequestDTO;
import br.com.fintrack.dto.TransacaoResponseDTO;
import br.com.fintrack.enums.CategoriaTransacao;
import br.com.fintrack.enums.TipoTransacao;
import br.com.fintrack.exception.RecursoNaoEncontradoException;
import br.com.fintrack.model.Transacao;
import br.com.fintrack.repository.TransacaoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransacaoService {

    //===============================
    private final TransacaoRepository transacaoRepository;
    //===============================

    public TransacaoService(TransacaoRepository repository) {
        this.transacaoRepository = repository;
    }

    public Transacao salvar(Transacao transacao) {
        return transacaoRepository.save(transacao);
    }

    public List<Transacao> listar() {
        return transacaoRepository.findAll();
    }

    public Optional<Transacao> buscarPorId(Long id) {
        return transacaoRepository.findById(id);
    }

    public Transacao buscarObrigatorioPorId(Long id) {
        return transacaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Transação não encontrada com o ID: " + id
                ));
    }

    public void excluir(Long id) {
        transacaoRepository.deleteById(id);
    }

    public Optional<Transacao> atualizar(Long id, Transacao dadosAtualizados) {
        return transacaoRepository.findById(id)
                .map(transacao -> {
                    transacao.setDescricao(dadosAtualizados.getDescricao());
                    transacao.setValor(dadosAtualizados.getValor());
                    transacao.setTipo(dadosAtualizados.getTipo());
                    transacao.setData(dadosAtualizados.getData());
                    transacao.setCategoria(dadosAtualizados.getCategoria());

                    return transacaoRepository.save(transacao);
                });

    }

    public List<Transacao> listarPorTipo(TipoTransacao tipo) {
        return transacaoRepository.findByTipo(tipo);

    }

    public BigDecimal somarPorTipo(TipoTransacao tipo) {
        return transacaoRepository.somaPorTipo(tipo);
    }

    public BigDecimal somaPorTipoNoPeriodo(
            TipoTransacao tipo,
            LocalDate inicio,
            LocalDate fim
    ) {
        return transacaoRepository.somaPorTipoNoPeriodo(tipo, inicio, fim);
    }

    public List<RelatorioCategoriaDTO> somaPorCategoria(TipoTransacao tipo) {
        return transacaoRepository.somarPorCategoria(tipo)
                .stream()
                .map(resultado -> new RelatorioCategoriaDTO(
                        (CategoriaTransacao) resultado[0],
                        (BigDecimal) resultado[1]
                ))
                .toList();
    }

    public List<Transacao> listarPorPeriodo(
            LocalDate inicio,
            LocalDate fim
    ) {
        return transacaoRepository.findByDataBetween(inicio, fim);
    }

    public List<Transacao> listarOrdenadasPorData() {
        return transacaoRepository.findAllByOrderByDataDesc();
    }

    public Page<Transacao> listarPaginadas(Pageable pageable) {
        return transacaoRepository.findAll(pageable);
    }


    private Transacao converteParaEntidade (TransacaoRequestDTO dto) {

        Transacao transacao = new Transacao();

        transacao.setDescricao(dto.getDescricao());
        transacao.setValor(dto.getValor());
        transacao.setTipo(dto.getTipo());
        transacao.setData(dto.getData());
        transacao.setCategoria(dto.getCategoria());

        return transacao;
    }

    private TransacaoResponseDTO converteParaResponseDTO(Transacao transacao) {

        return new TransacaoResponseDTO(
                transacao.getId(),
                transacao.getDescricao(),
                transacao.getValor(),
                transacao.getTipo(),
                transacao.getData(),
                transacao.getCategoria()
        );

    }

    public Transacao converteParaCadastro(TransacaoRequestDTO dto) {
        Transacao transacao = converteParaEntidade(dto);

        return transacaoRepository.save(transacao);
    }

    public TransacaoResponseDTO converteParaResponse(Transacao dto) {
        return converteParaResponseDTO(dto);
    }


    public Optional<TransacaoResponseDTO> atualizarComDTO(
            Long id,
            TransacaoRequestDTO dto
    ) {
        return transacaoRepository.findById(id)
                .map(transacao -> {
                    transacao.setDescricao(dto.getDescricao());
                    transacao.setValor(dto.getValor());
                    transacao.setTipo(dto.getTipo());
                    transacao.setData(dto.getData());
                    transacao.setCategoria(dto.getCategoria());

                    Transacao atualizada = transacaoRepository.save(transacao);

                    return converteParaResponseDTO(atualizada);
                });
    }
}
