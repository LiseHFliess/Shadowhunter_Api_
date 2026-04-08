package com.senac.Shadowhunters.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Schema(description = "Representa a sede dos Caçadores de Sombras em uma cidade")
public class Instituto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome da cidade é obrigatório")
    @Schema(example = "Nova York")
    private String nomeCidade;

    @NotBlank(message = "O chefe do instituto deve ser informado")
    @Schema(example = "Maryse Lightwood")
    private String chefeDoInstituto;

    // REQUISITO: Relacionamento One-to-Many
    // Um Instituto possui vários personagens (moradores)
    // O 'mappedBy' indica que o mapeamento principal está no campo 'instituto' da classe Personagem
    @OneToMany(mappedBy = "instituto")
    private List<Personagem> moradores;
}