package br.com.fintrack.exception;

public class RecursoNaoEncontradoException extends RuntimeException{

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
