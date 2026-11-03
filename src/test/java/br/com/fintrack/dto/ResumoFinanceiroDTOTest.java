
package br.com.fintrack.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResumoFinanceiroDTOTest {

    @Test
    void deveCriarResumoFinanceiroDTO() {
        BigDecimal receitas = new BigDecimal("5000.00");
        BigDecimal despesas = new BigDecimal("2000.00");
        BigDecimal saldo = new BigDecimal("3000.00");

        ResumoFinanceiroDTO dto = new ResumoFinanceiroDTO(
                receitas, despesas, saldo
        );

        assertEquals(receitas, dto.getReceitas());
        assertEquals(despesas, dto.getDespesas());
        assertEquals(saldo, dto.getSaldo());
    }

    @Test
    void deveAlterarReceitas() {
        ResumoFinanceiroDTO dto = criarDTO();
        BigDecimal novoValor = new BigDecimal("6000.00");

        dto.setReceitas(novoValor);

        assertEquals(novoValor, dto.getReceitas());
    }

    @Test
    void deveAlterarDespesas() {
        ResumoFinanceiroDTO dto = criarDTO();
        BigDecimal novoValor = new BigDecimal("2500.00");

        dto.setDespesas(novoValor);

        assertEquals(novoValor, dto.getDespesas());
    }

    @Test
    void deveAlterarSaldo() {
        ResumoFinanceiroDTO dto = criarDTO();
        BigDecimal novoValor = new BigDecimal("3500.00");

        dto.setSaldo(novoValor);

        assertEquals(novoValor, dto.getSaldo());
    }

    private ResumoFinanceiroDTO criarDTO() {
        return new ResumoFinanceiroDTO(
                new BigDecimal("5000.00"),
                new BigDecimal("2000.00"),
                new BigDecimal("3000.00")
        );
    }
}
