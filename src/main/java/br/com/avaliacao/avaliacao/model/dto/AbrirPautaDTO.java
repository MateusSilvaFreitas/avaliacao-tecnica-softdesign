package br.com.avaliacao.avaliacao.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbrirPautaDTO {
    private Integer tempo;
    private Long idPauta;
}
