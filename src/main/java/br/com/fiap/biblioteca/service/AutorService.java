package br.com.fiap.biblioteca.service;

import br.com.fiap.biblioteca.dto.AutorRequest;
import br.com.fiap.biblioteca.dto.AutorResponse;
import br.com.fiap.biblioteca.exception.RecursoNaoEncontradoException;
import br.com.fiap.biblioteca.exception.RegraNegocioException;
import br.com.fiap.biblioteca.model.Autor;
import br.com.fiap.biblioteca.repository.AutorRepository;
import br.com.fiap.biblioteca.repository.LivroRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AutorService {

    private final AutorRepository autorRepository;
    private final LivroRepository livroRepository;

    public AutorService(AutorRepository autorRepository, LivroRepository livroRepository) {
        this.autorRepository = autorRepository;
        this.livroRepository = livroRepository;
    }

    @Transactional(readOnly = true)
    public List<AutorResponse> listar(String nome) {
        List<Autor> autores = (nome == null || nome.isBlank())
                ? autorRepository.findAll(Sort.by("nome"))
                : autorRepository.findByNomeContainingIgnoreCase(nome);
        return autores.stream().map(AutorResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public AutorResponse buscarPorId(Long id) {
        return AutorResponse.de(buscarEntidade(id));
    }

    @Transactional
    public AutorResponse criar(AutorRequest request) {
        Autor autor = new Autor(request.nome(), request.nacionalidade());
        return AutorResponse.de(autorRepository.save(autor));
    }

    @Transactional
    public AutorResponse atualizar(Long id, AutorRequest request) {
        Autor autor = buscarEntidade(id);
        autor.setNome(request.nome());
        autor.setNacionalidade(request.nacionalidade());
        return AutorResponse.de(autorRepository.save(autor));
    }

    @Transactional
    public void excluir(Long id) {
        Autor autor = buscarEntidade(id);
        if (livroRepository.existsByAutorId(id)) {
            throw new RegraNegocioException("Não é possível excluir: o autor possui livros cadastrados");
        }
        autorRepository.delete(autor);
    }

    Autor buscarEntidade(Long id) {
        return autorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Autor não encontrado: id " + id));
    }
}
