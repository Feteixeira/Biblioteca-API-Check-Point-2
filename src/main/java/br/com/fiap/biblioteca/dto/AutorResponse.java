package br.com.fiap.biblioteca.dto;

import br.com.fiap.biblioteca.model.Autor;

public record AutorResponse(Long id, String nome, String nacionalidade) {

    public static AutorResponse de(Autor autor) {
        return new AutorResponse(autor.getId(), autor.getNome(), autor.getNacionalidade());
    }
}
