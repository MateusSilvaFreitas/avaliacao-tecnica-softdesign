package br.com.avaliacao.avaliacao.repository.pauta;

import br.com.avaliacao.avaliacao.model.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, Long> {

    @Query("SELECT p FROM pauta p " +
            "WHERE p.id = :idPauta AND CURRENT_TIMESTAMP() <= p.dataMaximaVotacao")
    Pauta verificarDataMaximaPauta(@Param("idPauta") Long idPauta);

    @Query("SELECT v.voto, COUNT(v) FROM pauta p " +
            "JOIN p.votos v " +
            "WHERE p.id = :idPauta " +
            "GROUP BY v.voto")
    List<Object[]> contarVotosPorPauta(@Param("idPauta") Long idPauta);

}
