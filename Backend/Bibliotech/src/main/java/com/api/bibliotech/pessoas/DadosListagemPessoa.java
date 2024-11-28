package com.api.bibliotech.pessoas;

public record DadosListagemPessoa(Long id, String nome, String email, String telefone) {
	public DadosListagemPessoa(Pessoa dados) {
		this(dados.getId(), dados.getNome(), dados.getEmail(), dados.getTelefone());
	}
}
