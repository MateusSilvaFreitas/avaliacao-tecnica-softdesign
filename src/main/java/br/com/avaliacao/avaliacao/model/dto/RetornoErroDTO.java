package br.com.avaliacao.avaliacao.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RetornoErroDTO {
    private String mensagem;
    private String detalhes;
    private LocalDateTime timestamp;

    public RetornoErroDTO(String mensagem, String detalhes) {
        this.mensagem = mensagem;
        this.detalhes = detalhes;
        this.timestamp = LocalDateTime.now();
    }
}

