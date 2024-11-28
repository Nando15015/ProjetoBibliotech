package com.api.bibliotech.livros;

public record DadosAlteracaoLivro(Long id, String titulo, String isbn, int ano_publicacao, Long id_genero, Long id_autor, String foto) {

}
