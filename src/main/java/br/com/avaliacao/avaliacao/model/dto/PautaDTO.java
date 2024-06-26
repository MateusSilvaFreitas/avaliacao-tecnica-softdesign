package br.com.avaliacao.avaliacao.model.dto;

import br.com.avaliacao.avaliacao.model.Pauta;
import lombok.Data;

@Data
public class PautaDTO {
    private Long id;
    private String nome;
    private Long votosSim;
    private Long votosNao;

    public PautaDTO(Pauta pauta){
        this.id = pauta.getId();
        this.nome = pauta.getNome();
        this.votosSim = pauta.getVotosSim();
        this.votosNao = pauta.getVotosNao();
    }
}
