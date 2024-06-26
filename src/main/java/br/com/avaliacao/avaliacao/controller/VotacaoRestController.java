package br.com.avaliacao.avaliacao.controller;

import br.com.avaliacao.avaliacao.model.dto.AbrirPautaDTO;
import br.com.avaliacao.avaliacao.model.dto.CriarPautaDTO;
import br.com.avaliacao.avaliacao.model.dto.PautaDTO;
import br.com.avaliacao.avaliacao.model.dto.VotoDTO;
import br.com.avaliacao.avaliacao.service.VotacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pauta")
public class VotacaoRestController {
    @Autowired
    private VotacaoService votacaoService;


    @PostMapping("/criar-pauta")
    public PautaDTO criarPauta(@RequestBody CriarPautaDTO criarPautaDTO){
        return votacaoService.criarPauta(criarPautaDTO);
    }

    @PostMapping("/abrir-pauta")
    public void abrirPauta(@RequestBody AbrirPautaDTO abrirPautaDTO){
        votacaoService.abrirPauta(abrirPautaDTO);
    }

    @PostMapping("/votar")
    public void votar(@RequestBody VotoDTO votoDTO){
        votacaoService.votar(votoDTO);
    }

    @GetMapping("/contabilizar-pauta/{idPauta}")
    public PautaDTO contabilizarPauta(@PathVariable Long idPauta){
        return votacaoService.contabilizarPauta(idPauta);
    }
}
