package br.com.avaliacao.avaliacao.exception;

public class PautaFinalizadaException extends RuntimeException{
    public PautaFinalizadaException(String mensagem){
        super(mensagem);
    }
}
