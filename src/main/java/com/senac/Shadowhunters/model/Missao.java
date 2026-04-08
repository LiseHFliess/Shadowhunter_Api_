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
@Schema(description = "Representa uma missão de caça a demônios ou patrulha")
public class Missao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A descrição da missão é obrigatória")
    @Schema(example = "Caçar demônio Shax no Brooklyn")
    private String descricao;

    @NotNull(message = "O nível de periculosidade deve ser informado")
    @Enumerated(EnumType.STRING) // Salva o nome do Enum no banco em vez do número
    @Schema(example = "ALTA")
    private NivelPericulosidade periculosidade;


    @NotBlank(message = "A localização é obrigatória")
    @Schema(example = "Hotel Dumort")
    private String localizacao;

    @ManyToMany
    @JoinTable(
            name = "missao_personagem",
            joinColumns = @JoinColumn(name = "missao_id"),
            inverseJoinColumns = @JoinColumn(name = "personagem_id")
    )
    private List<Personagem> personagensDesignados;

    // Construtor auxiliar sem ID para o banco inicial
    public Missao(String descricao, NivelPericulosidade periculosidade, String localizacao) {
        this.descricao = descricao;
        this.periculosidade = periculosidade;
        this.localizacao = localizacao;
    }
}