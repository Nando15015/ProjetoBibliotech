package com.api.bibliotech.reservas;

public record DadosListagemReserva(Long id, String data_reserva, String data_validade, Long id_livro, Long id_pessoa) {
	public DadosListagemReserva(Reserva dados) {
		this(dados.getId(), dados.getData_reserva(), dados.getData_validade(), dados.getId_livro(), dados.getId_pessoa());
	}
}
