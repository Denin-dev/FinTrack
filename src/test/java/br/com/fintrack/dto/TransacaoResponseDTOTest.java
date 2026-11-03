package br.com.fintrack.dto;

import br.com.fintrack.enums.CategoriaTransacao;
import br.com.fintrack.enums.TipoTransacao;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TransacaoResponseDTOTest {

    @Test
    void deveCriarTransacaoResponseDTOComSucesso() {
        TransacaoResponseDTO dto = new TransacaoResponseDTO();

        assertNull(dto.getId());
        assertNull(dto.getDescricao());
        assertNull(dto.getValor());
        assertNull(dto.getTipo());
        assertNull(dto.getData());
        assertNull(dto.getCategoria());
    }

    @Test
    void deveCriarTransacaoResponseDTO() {
        Long id = 1L;
        String descricao = "Pagamento de conta";
        BigDecimal valor = new BigDecimal("150.00");
        TipoTransacao tipo = TipoTransacao.DESPESA;
        LocalDate data = LocalDate.of(2026, 10, 15);
        CategoriaTransacao categoria = CategoriaTransacao.ALIMENTACAO;

        TransacaoResponseDTO dto = new TransacaoResponseDTO(
                id, descricao, valor, tipo, data, categoria
        );

        assertEquals(id, dto.getId());
        assertEquals(descricao, dto.getDescricao());
        assertEquals(valor, dto.getValor());
        assertEquals(tipo, dto.getTipo());
        assertEquals(data, dto.getData());
        assertEquals(categoria, dto.getCategoria());
    }
}
