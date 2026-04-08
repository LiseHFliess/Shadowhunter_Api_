package com.senac.Shadowhunters.Controller;

import com.senac.Shadowhunters.model.Instituto;
import com.senac.Shadowhunters.repository.InstitutoRepository;
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
@RequestMapping("/institutos")
@Tag(name = "Instituto", description = "Gerenciamento das sedes por cidade")
public class InstitutoController {

    @Autowired
    private InstitutoRepository repository;

    @Autowired
    private PagedResourcesAssembler<Instituto> assembler;

    @GetMapping
    @Operation(summary = "Listar todos os institutos (Paginado)")
    public ResponseEntity<PagedModel<EntityModel<Instituto>>> listar(Pageable pageable) {
        Page<Instituto> institutos = repository.findAll(pageable);
        return ResponseEntity.ok(assembler.toModel(institutos,
                i -> EntityModel.of(i, linkTo(methodOn(InstitutoController.class).buscar(i.getId())).withSelfRel())));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um instituto pelo ID")
    public ResponseEntity<EntityModel<Instituto>> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(i -> ResponseEntity.ok(EntityModel.of(i,
                        linkTo(methodOn(InstitutoController.class).buscar(id)).withSelfRel())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo instituto")
    public ResponseEntity<Instituto> criar(@Valid @RequestBody Instituto instituto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(instituto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza os dados de um instituto existente")
    public ResponseEntity<Instituto> atualizar(@PathVariable Long id, @Valid @RequestBody Instituto novo) {
        return repository.findById(id).map(i -> {
            i.setNomeCidade(novo.getNomeCidade());
            i.setChefeDoInstituto(novo.getChefeDoInstituto());
            return ResponseEntity.ok(repository.save(i));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deleta um instituto pelo ID")
    public void deletar(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @GetMapping("/cidade")
    @Operation(summary = "Consulta Personalizada: Busca por nome de cidade")
    public ResponseEntity<PagedModel<EntityModel<Instituto>>> buscarPorCidade(
            @RequestParam String nomeCidade, Pageable pageable) {
        return ResponseEntity.ok(assembler.toModel(
                repository.findByNomeCidadeContainingIgnoreCase(nomeCidade, pageable),
                i -> EntityModel.of(i, linkTo(methodOn(InstitutoController.class).buscar(i.getId())).withSelfRel())));
    }
}