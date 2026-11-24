package com.app.carteirinha.vencedores.repositories;

import com.app.carteirinha.vencedores.entities.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Todas as consultas ignoram membros com soft delete (deletedAt preenchido).
 * O @EntityGraph traz a igreja no mesmo SELECT (JOIN), evitando uma consulta extra por membro.
 */
public interface MemberRepository extends JpaRepository<Member, Long> {

    @EntityGraph(attributePaths = "church")
    List<Member> findAllByDeletedAtIsNull();

    @EntityGraph(attributePaths = "church")
    List<Member> findAllByChurchIdAndDeletedAtIsNull(Long churchId);

    @EntityGraph(attributePaths = "church")
    Optional<Member> findByIdAndDeletedAtIsNull(Long id);
}



