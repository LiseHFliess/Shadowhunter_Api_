package com.senac.Shadowhunters.model; 

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Representa as marcas angelicais (Runas) que concedem habilidades")
public class Runa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome da runa é obrigatório")
    @Schema(example = "Visão")
    private String nome;

    @NotBlank(message = "O efeito da runa deve ser descrito")
    @Schema(example = "Permite ver através de glamours e ilusões")
    private String efeito;

    @Min(value = 1, message = "O nível de dor mínimo é 1")
    @Max(value = 10, message = "O nível de dor máximo é 10")
    @Schema(example = "3")
    private int nivelDeDor;

    // Relacionamento 1:1 - Referência inversa para a Runa Principal
    // (Opcional, mas ajuda na navegabilidade do HATEOAS)
    @OneToOne(mappedBy = "runaPrincipal")
    private Personagem portadorPrincipal;

    // Construtor auxiliar
    public Runa(String nome, String efeito, int nivelDeDor) {
        this.nome = nome;
        this.efeito = efeito;
        this.nivelDeDor = nivelDeDor;
    }
}