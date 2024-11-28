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

import com.api.bibliotech.reservas.DadosAlteracaoReserva;
import com.api.bibliotech.reservas.DadosCadastroReserva;
import com.api.bibliotech.reservas.DadosListagemReserva;
import com.api.bibliotech.reservas.Reserva;
import com.api.bibliotech.reservas.ReservaRepository;
import com.api.bibliotech.livros.LivroRepository;
import com.api.bibliotech.pessoas.PessoaRepository;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/reservas")
public class ReservaController {
	@Autowired
	private ReservaRepository reservaRepository;
	
	@Autowired
	private LivroRepository livroRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	 
	@PostMapping
	@Transactional
	public ResponseEntity<?> cadastrar(@RequestBody DadosCadastroReserva dados) {
		if (!livroRepository.existsById(dados.id_livro())) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Livro não encontrado");
		}
		if (!pessoaRepository.existsById(dados.id_pessoa())) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa não encontrada");
		}
		var reserva = new Reserva(dados);
		reservaRepository.save(reserva);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(reserva.getId())
				.toUri();
		return ResponseEntity.created(location).body(reserva);
	}

	@GetMapping
	public ResponseEntity<List<DadosListagemReserva>> listar() {
		var lista = reservaRepository.findAll().stream().map(DadosListagemReserva::new).toList();
		return ResponseEntity.ok(lista);
	}

	@PutMapping
	@Transactional
	public ResponseEntity<?> alterar(@RequestBody DadosAlteracaoReserva dados) {
		if (!livroRepository.existsById(dados.id_livro())) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Livro não encontrado");
		}
		if (!pessoaRepository.existsById(dados.id_pessoa())) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa não encontrada");
		}
		if (!reservaRepository.existsById(dados.id())) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empréstimo não encontrado");
		}
		var reserva = reservaRepository.getReferenceById(dados.id());
		reserva.atualizaInformacoes(dados);
		return ResponseEntity.ok(dados);
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> excluir(@PathVariable Long id) {
		if (!reservaRepository.existsById(id)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empréstimo não encontrado");
		}
		reservaRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> detalhar(@PathVariable Long id) {
		if (!reservaRepository.existsById(id)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empréstimo não encontrado");
		}
		var reserva = reservaRepository.getReferenceById(id);
		DadosListagemReserva dados = new DadosListagemReserva(reserva);
		return ResponseEntity.ok(dados);
	}
}
