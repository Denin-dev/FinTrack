package br.com.fintrack.exception;

import br.com.fintrack.dto.ErroValidacaoDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void configurar() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void deveTratarViolacaoDeConstraint() {

        ConstraintViolation<?> violacao = mock(ConstraintViolation.class);
        Path caminho = mock(Path.class);

        when(caminho.toString()).thenReturn("valor");
        when(violacao.getPropertyPath()).thenReturn(caminho);
        when(violacao.getMessage()).thenReturn("O valor deve ser positivo");

        ConstraintViolationException exception = new ConstraintViolationException(Set.of(violacao));

        ResponseEntity<?> resposta = handler.tratarValidacao(exception);

        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        assertNotNull(resposta.getBody());
        assertTrue(resposta.getBody().toString().contains("valor"));
    }

    @Test
    void deveTratarErroDeArgumentoInvalido() {

        Object objeto = new Object();

        BeanPropertyBindingResult resultado = new BeanPropertyBindingResult(objeto, "objeto");

        resultado.addError(new FieldError("objeto", "descição", "A descrição é obrigatória"));

        resultado.addError(new FieldError("objeto", "valor", "O valor deve ser positivo"));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, resultado);

        ResponseEntity<String> resposta = handler.tratarErroValidacao(exception);

        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());

        assertTrue(resposta.getBody().contains("A descrição é obrigatória"));
        assertTrue(resposta.getBody().contains("O valor deve ser positivo"));
    }

    @Test
    void deveRemoverMensagensDeErroDuplicadas() {

        Object objeto = new Object();

        BeanPropertyBindingResult resultado =
                new BeanPropertyBindingResult(objeto, "objeto");

        resultado.addError(new FieldError(
                "objeto", "descricao", "Campo obrigatório"
        ));

        resultado.addError(new FieldError(
                "objeto", "categoria", "Campo obrigatório"
        ));

        MethodArgumentNotValidException exception =
                new MethodArgumentNotValidException(null, resultado);

        ResponseEntity<String> resposta =
                handler.tratarErroValidacao(exception);

        assertEquals("Campo obrigatório", resposta.getBody());
    }

    @Test
    void deveTratarRecursoNaoEncontrado() {

        RecursoNaoEncontradoException exception =
                new RecursoNaoEncontradoException("Transação não encontrada");

        ResponseEntity<?> resposta =
                handler.tratarRecursoNaoEncontrado(exception);

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
        assertTrue(resposta.getBody().toString()
                .contains("Transação não encontrada"));
    }
}
