package br.com.fintrack.dto;

import br.com.fintrack.enums.CategoriaTransacao;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class RelatorioCategoriaDTOTest {

    @Test
    void deveCriarRelatorioCategoriaDTO() {

        CategoriaTransacao categoria = CategoriaTransacao.values()[0];
        BigDecimal total = new BigDecimal("1500.00");

        RelatorioCategoriaDTO dto = new RelatorioCategoriaDTO(categoria, total);

        assertEquals(categoria, dto.getCategoria());
        assertEquals(total, dto.getTotal());
    }


    @Test
    void deveAlterarCategoria() {

        RelatorioCategoriaDTO dto = new RelatorioCategoriaDTO(
                CategoriaTransacao.values()[0],
                new BigDecimal("1500.00")
        );

        dto.setCategoria(null);
        assertNull(dto.getCategoria());
    }

    @Test
    void deveAlterarTotal() {

        RelatorioCategoriaDTO dto = new RelatorioCategoriaDTO(
                CategoriaTransacao.values()[0],
                new BigDecimal("1500.00")
        );

        BigDecimal novoTotal = new BigDecimal("2500.00");

        dto.setTotal(novoTotal);
        assertEquals(novoTotal, dto.getTotal());
    }


}
