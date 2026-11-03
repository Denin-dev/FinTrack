package br.com.fintrack.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResumoPorPeriodoDTOTest {

    @Test
    void deveCriarUmResumoPorPeriodoDTO() {

        LocalDate inicio = LocalDate.of(2026,10,1);
        LocalDate fim = LocalDate.of(2026,10,31);

        BigDecimal receitas = new BigDecimal("5000.00");
        BigDecimal despesas = new BigDecimal("2000.00");
        BigDecimal saldo = new BigDecimal("3000.00");

        ResumoPorPeriodoDTO dto = new ResumoPorPeriodoDTO(inicio, fim, receitas, despesas, saldo);

        assertEquals(inicio, dto.getInicio());
        assertEquals(fim, dto.getFim());
        assertEquals(receitas, dto.getReceitas());
        assertEquals(despesas,dto.getDespesas());
        assertEquals(saldo, dto.getSaldo());
    }

    @Test
    void deveAlterarInicio() {

        ResumoPorPeriodoDTO dto = criarDTO();
        LocalDate novoInicio = LocalDate.of(2026,9,1);

        dto.setInicio(novoInicio);
        assertEquals(novoInicio, dto.getInicio());
    }

    @Test
    void deevAlterarFim() {

        ResumoPorPeriodoDTO dto = criarDTO();
        LocalDate novoFim = LocalDate.of(2026,9,30);

        dto.setFim(novoFim);

        assertEquals(novoFim, dto.getFim());
    }

    @Test
    void deveAlterarReceitas() {

        ResumoPorPeriodoDTO dto = criarDTO();
        BigDecimal novoValor = new BigDecimal("6000.00");

        dto.setReceitas(novoValor);

        assertEquals(novoValor, dto.getReceitas());
    }

    @Test
    void deveAlterarDespesas() {

        ResumoPorPeriodoDTO dto = criarDTO();
        BigDecimal novoValor = new BigDecimal("2500.00");

        dto.setDespesas(novoValor);

        assertEquals(novoValor, dto.getDespesas());
    }

    @Test
    void deveAlterarSaldo() {

        ResumoPorPeriodoDTO dto = criarDTO();
        BigDecimal novoValor = new BigDecimal("3500.00");

        dto.setSaldo(novoValor);

        assertEquals(novoValor, dto.getSaldo());
    }

    private ResumoPorPeriodoDTO criarDTO() {
        return new ResumoPorPeriodoDTO(
                LocalDate.of(2026,10,1),
                LocalDate.of(2026,10,31),
                new BigDecimal("5000.00"),
                new BigDecimal("2000.00"),
                new BigDecimal("3000.00")
        );
    }
}
