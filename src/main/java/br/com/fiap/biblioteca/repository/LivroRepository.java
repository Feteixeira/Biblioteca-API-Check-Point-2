package br.com.fiap.biblioteca.repository;

import br.com.fiap.biblioteca.model.Livro;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    @EntityGraph(attributePaths = "autor")
    List<Livro> findAllByOrderByTituloAsc();

    @EntityGraph(attributePaths = "autor")
    List<Livro> findByTituloContainingIgnoreCaseOrderByTituloAsc(String titulo);

    @EntityGraph(attributePaths = "autor")
    List<Livro> findByAutorIdOrderByTituloAsc(Long autorId);

    @EntityGraph(attributePaths = "autor")
    Optional<Livro> findWithAutorById(Long id);

    boolean existsByIsbn(String isbn);

    boolean existsByIsbnAndIdNot(String isbn, Long id);

    boolean existsByAutorId(Long autorId);
}
