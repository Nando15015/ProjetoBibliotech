package com.api.bibliotech.emprestimos;

import com.api.bibliotech.livros.Livro;
import com.api.bibliotech.pessoas.Pessoa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "emprestimo")
@Entity(name = "emprestimos")
@Getter 
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Emprestimo {
	
	public Emprestimo(DadosCadastroEmprestimo dados, Livro livro, Pessoa pessoa) {
		this.data_emprestimo = dados.data_emprestimo();
		this.data_devolucao = dados.data_devolucao();
		this.livro = livro;
		this.pessoa = pessoa;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String data_emprestimo;
	private String data_devolucao;
	@ManyToOne
	@JoinColumn(name = "id_livro")
	private Livro livro;
	@ManyToOne
	@JoinColumn(name = "id_pessoa")
	private Pessoa pessoa;
	
	public void atualizaInformacoes(DadosAlteracaoEmprestimo dados, Livro livro, Pessoa pessoa) {
		if (dados.data_emprestimo() != null) {
			this.data_emprestimo = dados.data_emprestimo();
		}
		if (dados.data_devolucao() != null) {
			this.data_devolucao = dados.data_devolucao();
		}
		if (livro != null) {
			this.livro = livro;
		}
		if (pessoa != null) {
			this.pessoa = pessoa;
		}
	}
	
	public void atualizaDataDevolucao(String data) {
		if (data != null) {
			this.data_devolucao = data;
		}
	}
}
