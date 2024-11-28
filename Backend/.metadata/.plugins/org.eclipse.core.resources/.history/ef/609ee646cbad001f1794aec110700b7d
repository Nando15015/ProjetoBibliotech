package com.api.bibliotech.emprestimos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record DadosListagemEmprestimoNoID(Long id, String emprestimo, String devolucao, String livro, String foto, String pessoa) {
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public DadosListagemEmprestimoNoID(Emprestimo dados) {
        this(
            dados.getId(), LocalDate.parse(dados.getData_emprestimo(), INPUT_FORMATTER).format(OUTPUT_FORMATTER), dados.getData_devolucao() != null ? LocalDate.parse(dados.getData_devolucao(), INPUT_FORMATTER).format(OUTPUT_FORMATTER) : "", dados.getLivro().getTitulo(), dados.getLivro().getFoto(), dados.getPessoa().getNome()
        );
    }
}