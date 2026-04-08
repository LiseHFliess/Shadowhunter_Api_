package com.senac.Shadowhunters.repository;



import com.senac.Shadowhunters.model.Arma;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArmaRepository extends JpaRepository<Arma, Long> {
    // Consulta personalizada: Busca por material
    Page<Arma> findByTipoMaterialContainingIgnoreCase(String material, Pageable pageable);
}