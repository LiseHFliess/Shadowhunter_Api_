package com.senac.Shadowhunters.repository;

import com.senac.Shadowhunters.model.Missao;
import com.senac.Shadowhunters.model.NivelPericulosidade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissaoRepository extends JpaRepository<Missao, Long> {
    // Consulta personalizada: Busca por periculosidade
    Page<Missao> findByPericulosidade(NivelPericulosidade periculosidade, Pageable pageable);
}