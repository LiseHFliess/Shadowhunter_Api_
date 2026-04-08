package com.senac.Shadowhunters.Controller;

import com.senac.Shadowhunters.model.Runa;
import com.senac.Shadowhunters.repository.RunaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/runas")
@Tag(name = "Runa", description = "Gerenciamento de Runas e seus efeitos")
public class RunaController {

    @Autowired
    private RunaRepository repository;

    @Autowired
    private PagedResourcesAssembler<Runa> assembler;

    @GetMapping
    @Operation(summary = "Listar todas as runas (Paginado)")
    public ResponseEntity<PagedModel<EntityModel<Runa>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(assembler.toModel(repository.findAll(pageable),
                r -> EntityModel.of(r, linkTo(methodOn(RunaController.class).findById(r.getId())).withSelfRel())));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma runa pelo ID")
    public ResponseEntity<EntityModel<Runa>> findById(@PathVariable Long id) {
        return repository.findById(id)
                .map(r -> ResponseEntity.ok(EntityModel.of(r, linkTo(methodOn(RunaController.class).findById(id)).withSelfRel())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Cadastra uma nova runa")
    public ResponseEntity<Runa> create(@Valid @RequestBody Runa r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(r));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma runa existente")
    public ResponseEntity<Runa> update(@PathVariable Long id, @Valid @RequestBody Runa newR) {
        return repository.findById(id).map(r -> {
            r.setNome(newR.getNome());
            r.setEfeito(newR.getEfeito());
            r.setNivelDeDor(newR.getNivelDeDor());
            return ResponseEntity.ok(repository.save(r));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deleta uma runa pelo ID")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @GetMapping("/efeito")
    @Operation(summary = "Consulta Personalizada: Busca por efeito")
    public ResponseEntity<PagedModel<EntityModel<Runa>>> findByEfeito(@RequestParam String efeito, Pageable pageable) {
        return ResponseEntity.ok(assembler.toModel(repository.findByEfeitoContainingIgnoreCase(efeito, pageable)));
    }
}