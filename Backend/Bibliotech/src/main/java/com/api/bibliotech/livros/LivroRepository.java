package com.api.bibliotech.livros;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LivroRepository extends JpaRepository<Livro, Long> {
	List<Livro> findByTituloContaining(String search);
}
