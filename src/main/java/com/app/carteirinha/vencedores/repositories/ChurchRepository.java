package com.app.carteirinha.vencedores.repositories;


import com.app.carteirinha.vencedores.entities.Church;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChurchRepository extends JpaRepository<Church, UUID> {
}

