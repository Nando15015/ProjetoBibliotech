package com.api.bibliotech.reservas;

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

@Table(name = "reserva")
@Entity(name = "reservas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Reserva {
	
	public Reserva(DadosCadastroReserva dados) {
		this.data_reserva = dados.data_reserva();
		this.data_validade = dados.data_validade();
		this.id_livro = dados.id_livro();
		this.id_pessoa = dados.id_pessoa();
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String data_reserva;
	private String data_validade;
	private Long id_livro;
	private Long id_pessoa;
	
	public void atualizaInformacoes(DadosAlteracaoReserva dados) {
		if (dados.data_reserva() != null) {
			this.data_reserva = dados.data_reserva();
		}
		if (dados.data_validade() != null) {
			this.data_validade = dados.data_validade();
		}
		if (dados.id_livro() != null) {
			this.id_livro = dados.id_livro();
		}
		if (dados.id_pessoa() != null) {
			this.id_pessoa = dados.id_pessoa();
		}
	}
}
