package com.senac.Shadowhunters.model; 


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Representa uma arma (Lâmina Serafim, Arco, etc) de um personagem")
public class Arma {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "O nome da arma é obrigatório")
    @Schema(example = "Lâmina Serafim")
    private String nome;

    @NotBlank(message = "O tipo de material é obrigatório")
    @Schema(example = "Adamas")
    private String tipoMaterial;

    // Relacionamento Many-to-One
    // Várias armas podem pertencer ao mesmo Personagem (dono)
    @ManyToOne
    @JoinColumn(name = "personagem_id")
    private Personagem dono;
}