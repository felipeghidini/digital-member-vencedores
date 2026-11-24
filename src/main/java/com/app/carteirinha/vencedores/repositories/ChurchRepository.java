package com.app.carteirinha.vencedores.repositories;


import com.app.carteirinha.vencedores.entities.Church;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChurchRepository extends JpaRepository<Church, Long> {
}

