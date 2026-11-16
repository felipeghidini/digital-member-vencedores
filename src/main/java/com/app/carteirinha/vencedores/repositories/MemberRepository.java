package com.app.carteirinha.vencedores.repositories;

import com.app.carteirinha.vencedores.entities.Member;
import com.app.carteirinha.vencedores.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByStatus(Status status);
}



