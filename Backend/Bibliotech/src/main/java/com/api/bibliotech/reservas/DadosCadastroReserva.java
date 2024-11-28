package com.api.bibliotech.reservas;

public record DadosCadastroReserva(Long id, String data_reserva, String data_validade, Long id_livro, Long id_pessoa) {

}
