package com.app.carteirinha.vencedores.dtos;

import com.app.carteirinha.vencedores.entities.Church;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChurchResponseDTO(
        Long id,
        String name,
        String city,
        String address,
        LocalDateTime createdAt
) {
    public static ChurchResponseDTO from(Church church) {
        return new ChurchResponseDTO(
                church.getId(),
                church.getName(),
                church.getCity(),
                church.getAddress(),
                church.getCreatedAt()
        );
    }
}
