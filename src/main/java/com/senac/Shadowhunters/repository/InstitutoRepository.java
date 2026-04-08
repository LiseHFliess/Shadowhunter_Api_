package com.senac.Shadowhunters.repository;


import com.senac.Shadowhunters.model.Instituto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitutoRepository extends JpaRepository<Instituto, Long> {
    // Consulta personalizada: Busca por cidade
    Page<Instituto> findByNomeCidadeContainingIgnoreCase(String nomeCidade, Pageable pageable);
}