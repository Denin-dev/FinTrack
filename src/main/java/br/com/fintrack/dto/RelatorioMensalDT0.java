package br.com.fintrack.dto;

import java.math.BigDecimal;

public class RelatorioMensalDT0 {

    private int ano;
    private int mes;
    private BigDecimal receitas;
    private BigDecimal despesas;
    private BigDecimal saldo;

    public RelatorioMensalDT0(int ano, int mes, BigDecimal receitas, BigDecimal despesas, BigDecimal saldo) {
        this.ano = ano;
        this.mes = mes;
        this.receitas = receitas;
        this.despesas = despesas;
        this.saldo = saldo;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
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
