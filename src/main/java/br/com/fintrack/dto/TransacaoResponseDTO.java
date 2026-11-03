
package br.com.fintrack.dto;

import br.com.fintrack.enums.CategoriaTransacao;
import br.com.fintrack.enums.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransacaoResponseDTO {

    private Long id;
    private String descricao;
    private BigDecimal valor;
    private TipoTransacao tipo;
    private LocalDate data;
    private CategoriaTransacao categoria;

    public TransacaoResponseDTO() {
    }

    public TransacaoResponseDTO(
            Long id,
            String descricao,
            BigDecimal valor,
            TipoTransacao tipo,
            LocalDate data,
            CategoriaTransacao categoria
    ) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.tipo = tipo;
        this.data = data;
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public TipoTransacao getTipo() {
        return tipo;
    }

    public LocalDate getData() {
        return data;
    }

    public CategoriaTransacao getCategoria() {
        return categoria;
    }
}
