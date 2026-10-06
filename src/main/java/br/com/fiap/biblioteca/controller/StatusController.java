package br.com.fiap.biblioteca.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;


@RestController
@RequestMapping("/status")
@Tag(name = "Status", description = "Diagnóstico da conexão com o banco")
public class StatusController {

    private final JdbcTemplate jdbcTemplate;

    public StatusController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/banco")
    @Operation(summary = "Mostra servidor, banco e versão do SQL Server conectado")
    public Map<String, Object> banco() {
        Map<String, Object> status = new LinkedHashMap<>();
        status.put("conectado", true);
        status.put("servidor", jdbcTemplate.queryForObject("SELECT @@SERVERNAME", String.class));
        status.put("banco", jdbcTemplate.queryForObject("SELECT DB_NAME()", String.class));
        status.put("versao", jdbcTemplate.queryForObject("SELECT @@VERSION", String.class));
        status.put("totalAutores", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM autores", Long.class));
        status.put("totalLivros", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM livros", Long.class));
        return status;
    }
}
