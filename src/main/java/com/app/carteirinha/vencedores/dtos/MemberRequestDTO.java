package com.app.carteirinha.vencedores.dtos;

import com.app.carteirinha.vencedores.enums.Cargo;
import com.app.carteirinha.vencedores.enums.Status;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record MemberRequestDTO(
    @NotBlank(message = "O nome é obrigatório") String name,
    @Past(message = "A data de nascimento deve estar no passado")
    LocalDate birthDate,
    String photoUrl,
    Cargo cargo,
    String rg,
    @PastOrPresent(message = "A data de batismo não pode estar no futuro") LocalDate baptismDate,
    String fatherName,
    String motherName,
    @Email(message = "E-mail inválido") String email,
    String phone,
    Status status,
    @NotNull(message = "A igreja é obrigatória") Long churchId
) {
}
