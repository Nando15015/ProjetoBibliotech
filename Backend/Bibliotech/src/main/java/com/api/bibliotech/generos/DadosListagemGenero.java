package com.api.bibliotech.generos;

public record DadosListagemGenero(Long id, String nome) {
	public DadosListagemGenero(Genero dados) {
		this(dados.getId(), dados.getNome());
	}
}
