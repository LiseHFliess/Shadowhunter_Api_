package com.senac.Shadowhunters.repository;



import com.senac.Shadowhunters.model.Runa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RunaRepository extends JpaRepository<Runa, Long> {
    // Consulta personalizada: Busca pelo efeito
    Page<Runa> findByEfeitoContainingIgnoreCase(String efeito, Pageable pageable);
}