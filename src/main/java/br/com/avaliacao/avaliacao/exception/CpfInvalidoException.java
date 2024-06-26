package br.com.avaliacao.avaliacao.exception;

public class CpfInvalidoException extends RuntimeException{
    public CpfInvalidoException(String mensagem){
        super(mensagem);
    }
}
