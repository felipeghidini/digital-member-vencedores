package com.app.carteirinha.vencedores.dtos;

import jakarta.validation.constraints.NotBlank;

public record ChurchRequestDTO(
        @NotBlank(message = "O nome é obrigatório") String name,
        @NotBlank(message = "A cidade é obrigatória") String city,
        String address
) {
}
