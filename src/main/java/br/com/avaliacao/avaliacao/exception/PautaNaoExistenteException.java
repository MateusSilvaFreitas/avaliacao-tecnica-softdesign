package br.com.avaliacao.avaliacao.exception;

public class PautaNaoExistenteException extends RuntimeException{
    public PautaNaoExistenteException(String mensagem){
        super(mensagem);
    }
}
