package com.app.carteirinha.vencedores.repositories;

import com.app.carteirinha.vencedores.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}

