package com.api.bibliotech.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.bibliotech.generos.DadosAlteracaoGenero;
import com.api.bibliotech.generos.DadosCadastroGenero;
import com.api.bibliotech.generos.DadosListagemGenero;
import com.api.bibliotech.generos.Genero;
import com.api.bibliotech.generos.GeneroRepository;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/generos")
public class GeneroController {
	@Autowired
	private GeneroRepository repository;

	@PostMapping
	@Transactional
	public ResponseEntity<?> cadastrar(@RequestBody DadosCadastroGenero dados) {
		var genero = new Genero(dados);
		repository.save(genero);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(genero.getId())
				.toUri();
		return ResponseEntity.created(location).body(genero);
	}

	@GetMapping
	public ResponseEntity<List<DadosListagemGenero>> listar() {
		var lista = repository.findAll().stream().map(DadosListagemGenero::new).toList();
		return ResponseEntity.ok(lista);
	}

	@PutMapping
	@Transactional
	public ResponseEntity<?> alterar(@RequestBody DadosAlteracaoGenero dados) {
		if (!repository.existsById(dados.id())) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Gênero não encontrado");
		}
		var genero = repository.getReferenceById(dados.id());
		genero.atualizaInformacoes(dados);
		return ResponseEntity.ok(dados);
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> excluir(@PathVariable Long id) {
		if (!repository.existsById(id)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Gênero não encontrado");
		}
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> detalhar(@PathVariable Long id) {
		if (!repository.existsById(id)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Gênero não encontrado");
		}
		var genero = repository.getReferenceById(id);
		DadosListagemGenero dados = new DadosListagemGenero(genero);
		return ResponseEntity.ok(dados);
	}
}
