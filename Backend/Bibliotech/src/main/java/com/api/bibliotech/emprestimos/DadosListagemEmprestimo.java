package com.api.bibliotech.emprestimos;

public record DadosListagemEmprestimo(Long id, String data_emprestimo, String data_devolucao, Long id_livro, Long id_pessoa) {
	public DadosListagemEmprestimo(Emprestimo dados) {
		this(dados.getId(), dados.getData_emprestimo(), dados.getData_devolucao(), dados.getLivro().getId(), dados.getPessoa().getId());
	}
}
