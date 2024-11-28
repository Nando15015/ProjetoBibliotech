package com.api.bibliotech.emprestimos;

public record DadosAlteracaoEmprestimo(Long id, String data_emprestimo, String data_devolucao, Long id_livro, Long id_pessoa) {

}
