package br.com.avaliacao.avaliacao.service;

import br.com.avaliacao.avaliacao.exception.CpfInvalidoException;
import br.com.avaliacao.avaliacao.model.dto.validador.ERetornoValidador;
import br.com.avaliacao.avaliacao.model.dto.validador.ValidadorCpfDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ValidacaoCpfService {

    @Autowired
    private RestTemplate restTemplate;

    public boolean validarCpfVoto(String cpf){
        try{
            ResponseEntity<ValidadorCpfDTO> response = restTemplate.getForEntity(String.format("https://user-info.herokuapp.com/users/%s", cpf), ValidadorCpfDTO.class);
            if(response.getBody() == null || response.getStatusCode().is4xxClientError()){
                throw new CpfInvalidoException(String.format("O cpf %s não é um CPF válido", cpf));
            }
            return response.getBody().getStatus().equals(ERetornoValidador.ABLE_TO_VOTE);
        } catch (Exception e){
            throw new CpfInvalidoException(String.format("O cpf %s não é um CPF válido", cpf));
        }
    }
}
