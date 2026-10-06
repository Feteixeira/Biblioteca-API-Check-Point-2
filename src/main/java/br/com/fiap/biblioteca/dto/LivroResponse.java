package br.com.fiap.biblioteca.dto;

import br.com.fiap.biblioteca.model.Livro;

public record LivroResponse(
        Long id,
        String titulo,
        String isbn,
        Integer anoPublicacao,
        Integer quantidadeDisponivel,
        AutorResponse autor
) {

    public static LivroResponse de(Livro livro) {
        return new LivroResponse(
                livro.getId(),
                livro.getTitulo(),
                livro.getIsbn(),
                livro.getAnoPublicacao(),
                livro.getQuantidadeDisponivel(),
                AutorResponse.de(livro.getAutor())
        );
    }
}
