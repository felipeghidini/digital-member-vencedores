package com.app.carteirinha.vencedores.dtos;

import com.app.carteirinha.vencedores.entities.Member;
import com.app.carteirinha.vencedores.enums.Status;
import com.app.carteirinha.vencedores.enums.Cargo;

import java.time.LocalDate;

public record MemberResponseDTO(
        Long id,
        String name,
        LocalDate birthDate,
        String photoUrl,
        Cargo cargo,
        String rg,
        LocalDate baptismDate,
        String fatherName,
        String motherName,
        String email,
        String phone,
        Status status,
        Long churchId,
        String churchName
) {
    /** Deve ser chamado dentro de uma transação: church é LAZY. */
    public static MemberResponseDTO from(Member member) {
        return new MemberResponseDTO(
                member.getId(),
                member.getName(),
                member.getBirthDate(),
                member.getPhotoUrl(),
                member.getCargo(),
                member.getRg(),
                member.getBaptismDate(),
                member.getFatherName(),
                member.getMotherName(),
                member.getEmail(),
                member.getPhone(),
                member.getStatus(),
                member.getChurch().getId(),
                member.getChurch().getName()
        );
    }
}
