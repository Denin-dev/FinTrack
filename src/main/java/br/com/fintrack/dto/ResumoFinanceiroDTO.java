package br.com.fintrack.dto;

import java.math.BigDecimal;

public class ResumoFinanceiroDTO {

    private BigDecimal receitas;
    private BigDecimal despesas;
    private BigDecimal saldo;

    public ResumoFinanceiroDTO(BigDecimal receitas, BigDecimal despesas, BigDecimal saldo) {
        this.receitas = receitas;
        this.despesas = despesas;
        this.saldo = saldo;
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
