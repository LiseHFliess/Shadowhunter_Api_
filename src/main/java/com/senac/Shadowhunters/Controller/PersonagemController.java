package com.senac.Shadowhunters.Controller;

import com.senac.Shadowhunters.model.Personagem;
import com.senac.Shadowhunters.repository.PersonagemRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@RequestMapping("/personagens")
@Tag(name = "Personagem", description = "Gerenciamento de Caçadores de Sombras e Seres do Submundo")
public class PersonagemController {

    @Autowired
    private PersonagemRepository repository;

    @Autowired
    private PagedResourcesAssembler<Personagem> assembler;

    @GetMapping
    @Operation(summary = "Listar todos os personagens (Paginado)")
    public ResponseEntity<PagedModel<EntityModel<Personagem>>> listarTodos(Pageable pageable) {
        Page<Personagem> personagens = repository.findAll(pageable);

        PagedModel<EntityModel<Personagem>> model = assembler.toModel(personagens,
                p -> EntityModel.of(p,
                        linkTo(methodOn(PersonagemController.class).buscarPorId(p.getId())).withSelfRel(),
                        linkTo(methodOn(PersonagemController.class).deletar(p.getId())).withRel("delete")));

        return ResponseEntity.ok(model);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um personagem pelo ID")
    public EntityModel<Personagem> buscarPorId(@PathVariable Long id) {
        Personagem p = repository.findById(id)
                .orElseThrow(() -> new java.util.NoSuchElementException("Personagem com ID " + id + " não encontrado no Instituto!"));

        return EntityModel.of(p,
                linkTo(methodOn(PersonagemController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(PersonagemController.class).listarTodos(Pageable.unpaged())).withRel("lista"));
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo personagem")
    @ApiResponse(responseCode = "201", description = "Personagem criado com sucesso")
    public ResponseEntity<EntityModel<Personagem>> criar(@Valid @RequestBody Personagem personagem) {
        Personagem salvo = repository.save(personagem);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(EntityModel.of(salvo,
                        linkTo(methodOn(PersonagemController.class).buscarPorId(salvo.getId())).withSelfRel()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um personagem existente")
    public ResponseEntity<Personagem> atualizar(@PathVariable Long id, @Valid @RequestBody Personagem novosDados) {
        return repository.findById(id).map(p -> {
            p.setNome(novosDados.getNome());
            p.setEspecie(novosDados.getEspecie());
            p.setEstaVivo(novosDados.isEstaVivo());
            return ResponseEntity.ok(repository.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deleta um personagem pelo ID")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/busca")
    @Operation(summary = "Consulta Personalizada: Busca por parte do nome")
    public ResponseEntity<PagedModel<EntityModel<Personagem>>> buscarPorNome(
            @RequestParam String nome, Pageable pageable) {
        Page<Personagem> busca = repository.findByNomeContainingIgnoreCase(nome, pageable);
        return ResponseEntity.ok(assembler.toModel(busca));
    }
}