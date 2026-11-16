package com.app.carteirinha.vencedores.repositories;

import com.app.carteirinha.vencedores.entities.User;
import com.app.carteirinha.vencedores.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByCpf(String cpf);
}



