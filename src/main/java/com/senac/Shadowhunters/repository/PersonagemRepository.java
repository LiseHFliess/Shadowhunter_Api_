package com.senac.Shadowhunters.repository;


import com.senac.Shadowhunters.model.Personagem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonagemRepository extends JpaRepository<Personagem, Long> {
    // Consulta personalizada: Busca personagens pelo nome
    Page<Personagem> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}