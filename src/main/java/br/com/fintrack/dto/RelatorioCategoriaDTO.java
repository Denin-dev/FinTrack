package br.com.fintrack.dto;

import br.com.fintrack.enums.CategoriaTransacao;

import java.math.BigDecimal;

public class RelatorioCategoriaDTO {

    private CategoriaTransacao categoria;
    private BigDecimal total;

    public RelatorioCategoriaDTO(CategoriaTransacao categoria, BigDecimal total) {
        this.categoria = categoria;
        this.total = total;
    }

    public CategoriaTransacao getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaTransacao categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
