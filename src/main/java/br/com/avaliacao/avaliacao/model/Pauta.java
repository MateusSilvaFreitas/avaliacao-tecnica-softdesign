package br.com.avaliacao.avaliacao.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "pauta")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "nome")
    public String nome;

    @Column(name = "data_maxima_votacao")
    public LocalDateTime dataMaximaVotacao;

    @OneToMany(mappedBy = "idPauta", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Voto> votos = new ArrayList<>();

    @Transient
    private long votosSim;

    @Transient
    private long votosNao;

    public Pauta(String nome){
        this.nome = nome;
    }

    public void iniciarVotacao(Integer tempoVotacao){
        this.dataMaximaVotacao = LocalDateTime.now().plusMinutes(tempoVotacao != null ? tempoVotacao : 1);
    }
}
