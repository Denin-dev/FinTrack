
package br.com.fintrack.dto;

public class ErroValidacaoDTO {

    private String campo;
    private String mensagem;

    public ErroValidacaoDTO(String campo, String mensagem) {
        this.campo = campo;
        this.mensagem = mensagem;
    }

    public String getCampo() {
        return campo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}

