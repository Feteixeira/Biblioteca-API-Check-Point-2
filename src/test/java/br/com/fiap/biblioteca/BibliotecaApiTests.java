package br.com.fiap.biblioteca;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BibliotecaApiTests {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    private long criarAutor(String nome) throws Exception {
        String body = mvc.perform(post("/autores").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"" + nome + "\",\"nacionalidade\":\"Brasileira\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn().getResponse().getContentAsString();
        return mapper.readTree(body).get("id").asLong();
    }

    private String livroJson(String titulo, String isbn, long autorId) {
        return "{\"titulo\":\"" + titulo + "\",\"isbn\":\"" + isbn + "\",\"anoPublicacao\":1899,"
                + "\"quantidadeDisponivel\":2,\"autorId\":" + autorId + "}";
    }

    @Test
    void crudCompletoDeLivro() throws Exception {
        long autorId = criarAutor("Machado de Assis");

        String criado = mvc.perform(post("/livros").contentType(MediaType.APPLICATION_JSON)
                        .content(livroJson("Dom Casmurro", "111", autorId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.autor.nome").value("Machado de Assis"))
                .andReturn().getResponse().getContentAsString();
        JsonNode livro = mapper.readTree(criado);
        long livroId = livro.get("id").asLong();

        mvc.perform(get("/livros/{id}", livroId)).andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Dom Casmurro"));
        mvc.perform(get("/livros").param("titulo", "casm")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isbn").value("111"));
        mvc.perform(get("/livros").param("autorId", String.valueOf(autorId))).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        mvc.perform(put("/livros/{id}", livroId).contentType(MediaType.APPLICATION_JSON)
                        .content(livroJson("Dom Casmurro (ed. revista)", "111", autorId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Dom Casmurro (ed. revista)"));

        mvc.perform(delete("/autores/{id}", autorId)).andExpect(status().isConflict());

        mvc.perform(delete("/livros/{id}", livroId)).andExpect(status().isNoContent());
        mvc.perform(get("/livros/{id}", livroId)).andExpect(status().isNotFound());
        mvc.perform(delete("/autores/{id}", autorId)).andExpect(status().isNoContent());
    }

    @Test
    void validacoesERegras() throws Exception {
        long autorId = criarAutor("George Orwell");

        mvc.perform(post("/livros").contentType(MediaType.APPLICATION_JSON).content("{\"titulo\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes").isArray());

        mvc.perform(post("/livros").contentType(MediaType.APPLICATION_JSON)
                .content(livroJson("1984", "222", autorId))).andExpect(status().isCreated());
        mvc.perform(post("/livros").contentType(MediaType.APPLICATION_JSON)
                .content(livroJson("1984 cópia", "222", autorId))).andExpect(status().isConflict());

        mvc.perform(post("/livros").contentType(MediaType.APPLICATION_JSON)
                .content(livroJson("Sem autor", "333", 9999))).andExpect(status().isNotFound());
        mvc.perform(get("/autores/9999")).andExpect(status().isNotFound());
    }
}
