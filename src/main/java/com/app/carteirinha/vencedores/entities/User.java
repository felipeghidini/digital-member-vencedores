package com.app.carteirinha.vencedores.entities;

import com.app.carteirinha.vencedores.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

// "users" e nao "user": USER e palavra reservada no PostgreSQL.
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Somente digitos (11), sem pontos e traco. */
    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    /** Sempre o hash (BCrypt/Argon2), nunca a senha em texto puro. */
    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.MEMBER;

    /**
     * Member 1 --- 0..1 User (coluna member_id).
     * A FK fica aqui: um Member sem login simplesmente nao tem linha em users.
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, unique = true)
    private Member member;

    /** Church 1 --- N User (coluna church_id) */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private Church church;
}