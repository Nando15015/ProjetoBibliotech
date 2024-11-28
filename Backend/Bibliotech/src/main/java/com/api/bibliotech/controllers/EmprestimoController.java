package com.api.bibliotech.controllers;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

import com.api.bibliotech.emprestimos.DadosAlteracaoEmprestimo;
import com.api.bibliotech.emprestimos.DadosCadastroEmprestimo;
import com.api.bibliotech.emprestimos.DadosListagemEmprestimo;
import com.api.bibliotech.emprestimos.DadosListagemEmprestimoNoID;
import com.api.bibliotech.emprestimos.Emprestimo;
import com.api.bibliotech.emprestimos.EmprestimoRepository;
import com.api.bibliotech.livros.Livro;
import com.api.bibliotech.livros.LivroRepository;
import com.api.bibliotech.pessoas.Pessoa;
import com.api.bibliotech.pessoas.PessoaRepository;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/emprestimos")
public class EmprestimoController {
	
	@Autowired
	private EmprestimoRepository emprestimoRepository;
	
	@Autowired
	private LivroRepository livroRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@PostMapping
	@Transactional
	public ResponseEntity<?> cadastrar(@RequestBody DadosCadastroEmprestimo dados) {
		Livro livro = livroRepository.findById(dados.id_livro()).orElse(null);
		if (livro == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Livro não encontrado");
		}
		Pessoa pessoa = pessoaRepository.findById(dados.id_pessoa()).orElse(null);
		if (pessoa == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa não encontrada");
		}
		var emprestimo = new Emprestimo(dados, livro, pessoa);
		emprestimoRepository.save(emprestimo);
	    if (livro != null) {
	    	livro.atualizaStatusLivro("EMPRESTADO");
	    	livroRepository.save(livro);
	    }
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(emprestimo.getId())
				.toUri();
		return ResponseEntity.created(location).body(emprestimo);
	}

	@GetMapping
	public ResponseEntity<List<DadosListagemEmprestimo>> listar() {
		var lista = emprestimoRepository.findAll().stream().map(DadosListagemEmprestimo::new).toList();
		return ResponseEntity.ok(lista);
	}

	@PutMapping
	@Transactional
	public ResponseEntity<?> alterar(@RequestBody DadosAlteracaoEmprestimo dados) {
		Livro livro = livroRepository.findById(dados.id_livro()).orElse(null);
		if (livro == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Livro não encontrado");
		}
		Pessoa pessoa = pessoaRepository.findById(dados.id_pessoa()).orElse(null);
		if (pessoa == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa não encontrada");
		}
		if (!emprestimoRepository.existsById(dados.id())) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empréstimo não encontrado");
		}
		var emprestimo = emprestimoRepository.getReferenceById(dados.id());
		emprestimo.atualizaInformacoes(dados, livro, pessoa);
		return ResponseEntity.ok(dados);
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> excluir(@PathVariable Long id) {
		if (!emprestimoRepository.existsById(id)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empréstimo não encontrado");
		}
		var emprestimo = emprestimoRepository.getReferenceById(id);
		Livro livro = livroRepository.findById(emprestimo.getId()).orElse(null);
	    if (livro != null) {
	    	livro.atualizaStatusLivro("DISPONIVEL");
	    	livroRepository.save(livro);
	    }
		emprestimoRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> detalhar(@PathVariable Long id) {
		if (!emprestimoRepository.existsById(id)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empréstimo não encontrado");
		}
		var emprestimo = emprestimoRepository.getReferenceById(id);
		DadosListagemEmprestimo dados = new DadosListagemEmprestimo(emprestimo);
		return ResponseEntity.ok(dados);
	}
	
	@PutMapping("/{id}/devolver")
	public ResponseEntity<?> devolverEmprestimo(@PathVariable Long id) {
	    Emprestimo emprestimo = emprestimoRepository.findById(id).orElse(null);
	    if (emprestimo == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empréstimo não encontrado");
	    }
	    LocalDate data = LocalDate.now();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    String dataFormatada = data.format(formatter);
	    emprestimo.atualizaDataDevolucao(dataFormatada);
	    Livro livro = livroRepository.findById(emprestimo.getLivro().getId()).orElse(null);
	    if (livro != null) {
	    	livro.atualizaStatusLivro("DISPONIVEL");
	    	livroRepository.save(livro);
	    }
	    emprestimoRepository.save(emprestimo);
	    return ResponseEntity.ok("Devolução registrada com sucesso");
	}
	
	@GetMapping("/noid")
	public ResponseEntity<List<DadosListagemEmprestimoNoID>> listarNoID() {
		var lista = emprestimoRepository.findAll().stream().map(DadosListagemEmprestimoNoID::new).toList();
		return ResponseEntity.ok(lista);
	}
	
}
