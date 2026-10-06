package br.com.fiap.biblioteca.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErroResponse(LocalDateTime timestamp, int status, String erro, List<String> detalhes) {
}
