package br.com.avaliacao.avaliacao.repository.associado;

import br.com.avaliacao.avaliacao.model.Associado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociadoRepository extends JpaRepository<Associado, Long> {
}
