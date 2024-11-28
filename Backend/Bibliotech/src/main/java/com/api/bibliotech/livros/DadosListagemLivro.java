package com.api.bibliotech.livros;

public record DadosListagemLivro(Long id, String titulo, String isbn, int ano_publicacao, Long id_genero, Long id_autor, String status, String foto) {
	public DadosListagemLivro(Livro dados) {
		this(dados.getId(), dados.getTitulo(), dados.getIsbn(), dados.getAno_publicacao(), dados.getId_genero(), dados.getId_autor(), dados.getStatus(), dados.getFoto());
	}
}
