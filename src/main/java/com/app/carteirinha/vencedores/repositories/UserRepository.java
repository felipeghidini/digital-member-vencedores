package com.app.carteirinha.vencedores.repositories;

import com.app.carteirinha.vencedores.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}

