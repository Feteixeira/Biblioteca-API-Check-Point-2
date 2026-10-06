package br.com.fiap.biblioteca.service;

import br.com.fiap.biblioteca.dto.LivroRequest;
import br.com.fiap.biblioteca.dto.LivroResponse;
import br.com.fiap.biblioteca.exception.RecursoNaoEncontradoException;
import br.com.fiap.biblioteca.exception.RegraNegocioException;
import br.com.fiap.biblioteca.model.Livro;
import br.com.fiap.biblioteca.repository.LivroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorService autorService;

    public LivroService(LivroRepository livroRepository, AutorService autorService) {
        this.livroRepository = livroRepository;
        this.autorService = autorService;
    }

    @Transactional(readOnly = true)
    public List<LivroResponse> listar(String titulo, Long autorId) {
        List<Livro> livros;
        if (autorId != null) {
            autorService.buscarEntidade(autorId); // 404 se o autor não existir
            livros = livroRepository.findByAutorIdOrderByTituloAsc(autorId);
        } else if (titulo != null && !titulo.isBlank()) {
            livros = livroRepository.findByTituloContainingIgnoreCaseOrderByTituloAsc(titulo);
        } else {
            livros = livroRepository.findAllByOrderByTituloAsc();
        }
        return livros.stream().map(LivroResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public LivroResponse buscarPorId(Long id) {
        return LivroResponse.de(buscarEntidade(id));
    }

    @Transactional
    public LivroResponse criar(LivroRequest request) {
        if (livroRepository.existsByIsbn(request.isbn())) {
            throw new RegraNegocioException("Já existe um livro com o ISBN " + request.isbn());
        }
        Livro livro = new Livro();
        preencher(livro, request);
        return LivroResponse.de(livroRepository.save(livro));
    }

    @Transactional
    public LivroResponse atualizar(Long id, LivroRequest request) {
        Livro livro = buscarEntidade(id);
        if (livroRepository.existsByIsbnAndIdNot(request.isbn(), id)) {
            throw new RegraNegocioException("Já existe outro livro com o ISBN " + request.isbn());
        }
        preencher(livro, request);
        return LivroResponse.de(livroRepository.save(livro));
    }

    @Transactional
    public void excluir(Long id) {
        livroRepository.delete(buscarEntidade(id));
    }

    private void preencher(Livro livro, LivroRequest request) {
        livro.setTitulo(request.titulo());
        livro.setIsbn(request.isbn());
        livro.setAnoPublicacao(request.anoPublicacao());
        livro.setQuantidadeDisponivel(request.quantidadeDisponivel());
        livro.setAutor(autorService.buscarEntidade(request.autorId()));
    }

    private Livro buscarEntidade(Long id) {
        return livroRepository.findWithAutorById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado: id " + id));
    }
}
