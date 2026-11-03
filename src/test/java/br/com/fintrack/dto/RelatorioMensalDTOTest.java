package br.com.fintrack.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RelatorioMensalDTOTest {

    @Test
    void deveCriarRelatorioMensalDTO() {
        int ano = 2026; // OK
        int mes = 10; // OK
        BigDecimal receitas = new BigDecimal("5000.00");
        BigDecimal despesas = new BigDecimal("2000.00");
        BigDecimal saldo = new BigDecimal("3000.00");

        RelatorioMensalDT0 dto = new RelatorioMensalDT0(
                ano, mes, receitas, despesas, saldo
        );

        assertEquals(ano,dto.getAno());
        assertEquals(mes,dto.getMes());
        assertEquals(receitas,dto.getReceitas());
        assertEquals(despesas, dto.getDespesas());
        assertEquals(saldo,dto.getSaldo());
    }

    @Test
    void deveAlterarAno() {
        RelatorioMensalDT0 dto = criarDTO();

            dto.setAno(2025);

            assertEquals(2025, dto.getAno());
    }

    @Test
    void deveAlterarMes() {
        RelatorioMensalDT0 dto = criarDTO();

        dto.setMes(9);

        assertEquals(9, dto.getMes());
    }

    @Test
    void deveAlterarReceitas() {

        RelatorioMensalDT0 dto = criarDTO();
        BigDecimal novoValor = new BigDecimal("6000.00");

        dto.setReceitas(novoValor);

        assertEquals(novoValor, dto.getReceitas());
    }

    @Test
    void deveAlterarDespesas() {

        RelatorioMensalDT0 dto = criarDTO();
        BigDecimal novoValor = new BigDecimal("2500.00");

        dto.setDespesas(novoValor);

        assertEquals(novoValor, dto.getDespesas());
    }

    @Test
    void deveAlterarSaldo() {
        RelatorioMensalDT0 dto = criarDTO();
        BigDecimal novoValor = new BigDecimal("3500.00");

        dto.setSaldo(novoValor);

        assertEquals(novoValor, dto.getSaldo());
    }

    private RelatorioMensalDT0 criarDTO() {
        return new RelatorioMensalDT0(
                2026,
                10,
                new BigDecimal("5000.00"),
                new BigDecimal("2000.00"),
                new BigDecimal("3000.00")
        );
    }
}
