package br.com.avaliacao.avaliacao.model.dto;

import br.com.avaliacao.avaliacao.model.ETipoVoto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VotoDTO {
    private Long idPauta;
    private Long idAssociado;
    private ETipoVoto voto;

}
