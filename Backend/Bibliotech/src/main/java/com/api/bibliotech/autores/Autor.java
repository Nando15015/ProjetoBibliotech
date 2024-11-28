package com.api.bibliotech.autores;

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

@Table(name = "autor")
@Entity(name = "autores")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Autor {
	
	public Autor(DadosCadastroAutor dados) {
		this.nome = dados.nome();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	
	public void atualizaInformacoes(DadosAlteracaoAutor dados) {
		if (dados.nome() != null) {
			this.nome = dados.nome();
		}
	}
}
