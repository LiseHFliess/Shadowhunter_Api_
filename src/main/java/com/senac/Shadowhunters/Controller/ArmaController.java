package com.senac.Shadowhunters.Controller;

import com.senac.Shadowhunters.model.Arma;
import com.senac.Shadowhunters.repository.ArmaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/armas")
@Tag(name = "Arma", description = "Gerenciamento de Armas e Materiais")
public class ArmaController {

    @Autowired
    private ArmaRepository repository;

    @GetMapping
    @Operation(summary = "Listar todas as armas (Paginado)")
    public ResponseEntity<Page<Arma>> findAll(Pageable pageable) {
        return ResponseEntity.ok(repository.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma arma pelo ID")
    public ResponseEntity<Arma> findById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Cadastra uma nova arma")
    public ResponseEntity<Arma> create(@Valid @RequestBody Arma a) {
        Arma saved = repository.save(a);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma arma existente")
    public ResponseEntity<Arma> update(@PathVariable Long id, @Valid @RequestBody Arma newA) {
        return repository.findById(id).map(a -> {
            a.setNome(newA.getNome());
            a.setTipoMaterial(newA.getTipoMaterial());
            return ResponseEntity.ok(repository.save(a));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta uma arma pelo ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/material")
    @Operation(summary = "Consulta Personalizada: Busca armas por tipo de material")
    public ResponseEntity<Page<Arma>> findByMaterial(@RequestParam String material, Pageable pageable) {
        return ResponseEntity.ok(repository.findByTipoMaterialContainingIgnoreCase(material, pageable));
    }
}