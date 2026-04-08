package com.senac.Shadowhunters.Controller;

import com.senac.Shadowhunters.model.Missao;
import com.senac.Shadowhunters.model.NivelPericulosidade;
import com.senac.Shadowhunters.repository.MissaoRepository;
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
@RequestMapping("/missoes")
@Tag(name = "Missão", description = "Gerenciamento de missões e níveis de periculosidade")
public class MissaoController {

    @Autowired
    private MissaoRepository repository;

    @Autowired
    private PagedResourcesAssembler<Missao> assembler;

    @GetMapping
    @Operation(summary = "Listar todas as missões (Paginado)")
    public ResponseEntity<PagedModel<EntityModel<Missao>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(assembler.toModel(repository.findAll(pageable),
                m -> EntityModel.of(m, linkTo(methodOn(MissaoController.class).findById(m.getId())).withSelfRel())));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma missão pelo ID")
    public ResponseEntity<EntityModel<Missao>> findById(@PathVariable Long id) {
        return repository.findById(id)
                .map(m -> ResponseEntity.ok(EntityModel.of(m, linkTo(methodOn(MissaoController.class).findById(id)).withSelfRel())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Cadastra uma nova missão")
    public ResponseEntity<Missao> create(@Valid @RequestBody Missao m) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(m));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma missão existente")
    public ResponseEntity<Missao> update(@PathVariable Long id, @Valid @RequestBody Missao newM) {
        return repository.findById(id).map(m -> {
            m.setDescricao(newM.getDescricao());
            m.setPericulosidade(newM.getPericulosidade());
            m.setLocalizacao(newM.getLocalizacao());
            return ResponseEntity.ok(repository.save(m));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deleta uma missão pelo ID")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @GetMapping("/perigo")
    @Operation(summary = "Consulta Personalizada: Busca por nível de perigo")
    public ResponseEntity<PagedModel<EntityModel<Missao>>> findByPerigo(@RequestParam NivelPericulosidade nivel, Pageable pageable) {
        return ResponseEntity.ok(assembler.toModel(repository.findByPericulosidade(nivel, pageable)));
    }
}