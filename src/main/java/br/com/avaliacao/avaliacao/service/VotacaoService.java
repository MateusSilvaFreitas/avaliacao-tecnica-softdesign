package br.com.avaliacao.avaliacao.service;

import br.com.avaliacao.avaliacao.exception.PautaFinalizadaException;
import br.com.avaliacao.avaliacao.exception.PautaJaIniciadaException;
import br.com.avaliacao.avaliacao.exception.PautaNaoExistenteException;
import br.com.avaliacao.avaliacao.exception.VotoRealizadoException;
import br.com.avaliacao.avaliacao.model.Associado;
import br.com.avaliacao.avaliacao.model.ETipoVoto;
import br.com.avaliacao.avaliacao.model.Pauta;
import br.com.avaliacao.avaliacao.model.Voto;
import br.com.avaliacao.avaliacao.model.dto.AbrirPautaDTO;
import br.com.avaliacao.avaliacao.model.dto.CriarPautaDTO;
import br.com.avaliacao.avaliacao.model.dto.PautaDTO;
import br.com.avaliacao.avaliacao.model.dto.VotoDTO;
import br.com.avaliacao.avaliacao.repository.associado.AssociadoRepository;
import br.com.avaliacao.avaliacao.repository.pauta.PautaRepository;
import br.com.avaliacao.avaliacao.repository.voto.VotoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VotacaoService {

    @Autowired
    PautaRepository pautaRepository;

    @Autowired
    VotoRepository votoRepository;

    @Autowired
    AssociadoRepository associadoRepository;

    @Autowired
    ValidacaoCpfService validacaoCpfService;

    @Transactional
    public PautaDTO criarPauta(CriarPautaDTO criarPautaDTO) {
        Pauta pauta = new Pauta(criarPautaDTO.getNome());
        pautaRepository.save(pauta);
        return new PautaDTO(pauta);
    }

    @Transactional
    public void abrirPauta(AbrirPautaDTO abrirPautaDTO) {
        Pauta pauta = pautaRepository.findById(abrirPautaDTO.getIdPauta()).orElseThrow(() -> new PautaNaoExistenteException(String.format("Não existe pauta com id %d", abrirPautaDTO.getIdPauta())));

        if (pauta.getDataMaximaVotacao() != null) {
            throw new PautaJaIniciadaException(String.format("A pauta %d - %s já foi iniciada", pauta.getId(), pauta.getNome()));
        }

        pauta.iniciarVotacao(abrirPautaDTO.getTempo());
        pautaRepository.save(pauta);
    }

    @Transactional
    public void votar(VotoDTO votoDTO) {
        Associado associado = associadoRepository.findById(votoDTO.getIdAssociado()).orElseThrow(() -> new IllegalArgumentException("O associado não possui registro na base de dados"));

//        if(!validacaoCpfService.validarCpfVoto(associado.getCpf())){
//            throw new CpfInvalidoException(String.format("O cpf %s não está liberado para a votação", associado.getCpf()));
//        }

        Voto voto = votoRepository.findByIdPautaAndIdAssociado(votoDTO.getIdPauta(), associado.getId());
        if (voto != null) {
            throw new VotoRealizadoException(String.format("O cpf %s já realizou um voto nessa pauta", associado.getCpf()));
        }

        Pauta pauta = pautaRepository.verificarDataMaximaPauta(votoDTO.getIdPauta());
        if (pauta == null) throw new PautaFinalizadaException("A pauta não está aberta para votação ou não existe");


        Voto votoNew = new Voto();
        votoNew.setVoto(votoDTO.getVoto());
        votoNew.setIdPauta(votoDTO.getIdPauta());
        votoNew.setIdAssociado(associado.getId());

        votoRepository.save(votoNew);
    }

    @Transactional
    public PautaDTO contabilizarPauta(Long idPauta) {
        Pauta pauta = pautaRepository.findById(idPauta).orElseThrow(() -> new PautaNaoExistenteException(String.format("Não existe pauta com id %d", idPauta)));

        List<Object[]> resultados = pautaRepository.contarVotosPorPauta(idPauta);

        for (Object[] resultado : resultados) {
            ETipoVoto tipo = (ETipoVoto) resultado[0];
            Long count = (Long) resultado[1];
            if (tipo.equals(ETipoVoto.SIM)) {
                pauta.setVotosSim(count);
            } else if (tipo.equals(ETipoVoto.NAO)) {
                pauta.setVotosNao(count);
            }
        }

        return new PautaDTO(pauta);
    }
}
