
package br.com.fintrack.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErroValidacaoDTOTest {

    @Test
    void deveCriarErroValidacaoDTO() {
        String campo = "descricao";
        String mensagem = "A descrição é obrigatória";

        ErroValidacaoDTO dto = new ErroValidacaoDTO(campo, mensagem);

        assertEquals(campo, dto.getCampo());
        assertEquals(mensagem, dto.getMensagem());
    }

    @Test
    void deveAlterarCampo() {
        ErroValidacaoDTO dto = criarDTO();

        dto.setCampo("valor");

        assertEquals("valor", dto.getCampo());
    }

    @Test
    void deveAlterarMensagem() {
        ErroValidacaoDTO dto = criarDTO();

        dto.setMensagem("O valor deve ser maior que zero");

        assertEquals("O valor deve ser maior que zero", dto.getMensagem());
    }

    private ErroValidacaoDTO criarDTO() {
        return new ErroValidacaoDTO(
                "descricao",
                "A descrição é obrigatória"
        );
    }
}
