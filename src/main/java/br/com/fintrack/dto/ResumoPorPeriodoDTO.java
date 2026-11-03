package br.com.fintrack.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ResumoPorPeriodoDTO {

    private LocalDate inicio;
    private LocalDate fim;
    private BigDecimal receitas;
    private BigDecimal despesas;
    private BigDecimal saldo;

    public ResumoPorPeriodoDTO(LocalDate inicio, LocalDate fim, BigDecimal receitas, BigDecimal despesas, BigDecimal saldo) {
        this.inicio = inicio;
        this.fim = fim;
        this.receitas = receitas;
        this.despesas = despesas;
        this.saldo = saldo;
    }

    public LocalDate getInicio() {
        return inicio;
    }

    public void setInicio(LocalDate inicio) {
        this.inicio = inicio;
    }

    public LocalDate getFim() {
        return fim;
    }

    public void setFim(LocalDate fim) {
        this.fim = fim;
    }

    public BigDecimal getReceitas() {
        return receitas;
    }

    public void setReceitas(BigDecimal receitas) {
        this.receitas = receitas;
    }

    public BigDecimal getDespesas() {
        return despesas;
    }

    public void setDespesas(BigDecimal despesas) {
        this.despesas = despesas;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}
