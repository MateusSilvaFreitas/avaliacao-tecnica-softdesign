package br.com.avaliacao.avaliacao.repository.voto;

import br.com.avaliacao.avaliacao.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotoRepository extends JpaRepository<Voto, Long> {
    Voto findByIdPautaAndIdAssociado(Long idPauta, Long idAssociado);
}
