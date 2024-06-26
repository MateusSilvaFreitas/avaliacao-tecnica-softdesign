package br.com.avaliacao.avaliacao.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "voto")
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_pauta")
    private Long idPauta;

    @Column(name = "id_associado")
    private Long idAssociado;


    @Column(name = "voto")
    @Enumerated(EnumType.STRING)
    private ETipoVoto voto;
}
