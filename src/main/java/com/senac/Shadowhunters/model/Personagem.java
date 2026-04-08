package com.senac.Shadowhunters.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Representa um Caçador de Sombras ou Ser do Submundo")
public class Personagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do personagem é obrigatório")
    @Schema(example = "Jace Herondale")
    private String nome;

    @NotNull(message = "A espécie deve ser informada")
    @Enumerated(EnumType.STRING)
    @Schema(example = "SHADOWHUNTER")
    private Especie especie;

    private boolean estaVivo = true;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "runa_id", referencedColumnName = "id")
    private Runa runaPrincipal;

    @ManyToOne
    @JoinColumn(name = "instituto_id")
    private Instituto instituto;

    // ATENÇÃO: O mappedBy deve ser "dono", porque na sua classe Arma o campo chama "dono"
    @OneToMany(mappedBy = "dono", cascade = CascadeType.ALL)
    private List<Arma> armas;

    @ManyToMany(mappedBy = "personagensDesignados")
    private List<Missao> missoes;

    public Personagem(String nome, Especie especie, boolean estaVivo) {
        this.nome = nome;
        this.especie = especie;
        this.estaVivo = estaVivo;
    }
}