package com.api.bibliotech.livros;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "livro")
@Entity(name = "livros")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Livro {
	
	public Livro(DadosCadastroLivro dados) {
		this.titulo = dados.titulo();
		this.isbn = dados.isbn();
		this.ano_publicacao = dados.ano_publicacao();
		this.id_genero = dados.id_genero();
		this.id_autor = dados.id_autor();
		this.status = "DISPONIVEL";
		this.foto = dados.foto();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String titulo;
	private String isbn;
	private int ano_publicacao;
	private Long id_genero;
	private Long id_autor;
	private String status;
	private String foto;
	
	public void atualizaInformacoes(DadosAlteracaoLivro dados) {
		if (dados.titulo() != null) {
			this.titulo = dados.titulo();
		}
		if (dados.isbn() != null) {
			this.isbn = dados.isbn();
		}
		if (dados.ano_publicacao() != 0) {
			this.ano_publicacao = dados.ano_publicacao();
		}
		if (dados.id_genero() != null) {
			this.id_genero = dados.id_genero();
		}
		if (dados.id_autor() != null) {
			this.id_autor = dados.id_autor();
		}
		if (dados.foto() != null) {
			this.foto = dados.foto();
		}
	}
	
	public void atualizaStatusLivro(String status) {
		this.status = status;
	}
}
