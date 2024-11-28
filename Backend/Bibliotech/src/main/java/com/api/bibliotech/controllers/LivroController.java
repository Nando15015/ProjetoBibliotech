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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.bibliotech.autores.AutorRepository;
import com.api.bibliotech.generos.GeneroRepository;
import com.api.bibliotech.livros.DadosAlteracaoLivro;
import com.api.bibliotech.livros.DadosCadastroLivro;
import com.api.bibliotech.livros.DadosListagemLivro;
import com.api.bibliotech.livros.Livro;
import com.api.bibliotech.livros.LivroRepository;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/livros")
public class LivroController {
	@Autowired
	private LivroRepository livroRepository;
	
	@Autowired
	private AutorRepository autorRepository;
	
	@Autowired
	private GeneroRepository generoRepository;
	
	@PostMapping
	@Transactional
	public ResponseEntity<?> cadastrar(@RequestBody DadosCadastroLivro dados) {
		if (!autorRepository.existsById(dados.id_autor())) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Autor não encontrado");
		}
		if (!generoRepository.existsById(dados.id_genero())) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Gênero não encontrado");
		}
		var livro = new Livro(dados);
		livroRepository.save(livro);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(livro.getId())
				.toUri();
		return ResponseEntity.created(location).body(livro);
	}
	
	@GetMapping
	public ResponseEntity<List<DadosListagemLivro>> listar(@RequestParam(required = false) String search) {
		List<DadosListagemLivro> lista;
		if (search != null) {
			lista = livroRepository.findByTituloContaining(search).stream().map(DadosListagemLivro::new).toList();
		} else {
			lista = livroRepository.findAll().stream().map(DadosListagemLivro::new).toList();
		}
        return ResponseEntity.ok(lista);
	}
	
	@PutMapping
	@Transactional
	public ResponseEntity<?> alterar(@RequestBody DadosAlteracaoLivro dados) {
		if (!autorRepository.existsById(dados.id_autor())) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Autor não encontrado");
		}
		if (!generoRepository.existsById(dados.id_genero())) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Gênero não encontrado");
		}
		if (!livroRepository.existsById(dados.id())) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Livro não encontrado");
		}
		var livro = livroRepository.getReferenceById(dados.id());
		livro.atualizaInformacoes(dados);
		return ResponseEntity.ok(dados);
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> excluir(@PathVariable Long id) {
		if (!livroRepository.existsById(id)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Livro não encontrado");
		}
		livroRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{id}")
	@Transactional
	public ResponseEntity<?> detalhar(@PathVariable Long id) {
		if (!livroRepository.existsById(id)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Livro não encontrado");
		}
		var livro = livroRepository.getReferenceById(id);
		DadosListagemLivro dados = new DadosListagemLivro(livro);
		return ResponseEntity.ok(dados);
	}
}
