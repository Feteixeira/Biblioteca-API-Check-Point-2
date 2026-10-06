package br.com.fiap.biblioteca.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record LivroRequest(
        @NotBlank(message = "O título é obrigatório")
        @Size(max = 200, message = "O título deve ter no máximo 200 caracteres")
        String titulo,

        @NotBlank(message = "O ISBN é obrigatório")
        @Size(max = 20, message = "O ISBN deve ter no máximo 20 caracteres")
        String isbn,

        @Min(value = 1000, message = "Ano de publicação inválido")
        @Max(value = 2100, message = "Ano de publicação inválido")
        Integer anoPublicacao,

        @NotNull(message = "A quantidade disponível é obrigatória")
        @PositiveOrZero(message = "A quantidade disponível não pode ser negativa")
        Integer quantidadeDisponivel,

        @NotNull(message = "O autorId é obrigatório")
        Long autorId
) {
}
