package com.app.carteirinha.vencedores.services;

import com.app.carteirinha.vencedores.dtos.MemberRequestDTO;
import com.app.carteirinha.vencedores.dtos.MemberResponseDTO;
import com.app.carteirinha.vencedores.entities.Member;
import com.app.carteirinha.vencedores.enums.Cargo;
import com.app.carteirinha.vencedores.enums.Status;
import com.app.carteirinha.vencedores.exceptions.ResourceNotFoundException;
import com.app.carteirinha.vencedores.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository repository;
    private final ChurchService churchService;

    @Transactional(readOnly = true)
    public List<MemberResponseDTO> findAll(Long churchId) {
        List<Member> members = (churchId == null)
                ? repository.findAllByDeletedAtIsNull()
                : repository.findAllByChurchIdAndDeletedAtIsNull(churchId);
        return members.stream().map(MemberResponseDTO::from).toList();
    }

    @Transactional(readOnly = true)
    public MemberResponseDTO findById(Long id) {
        return MemberResponseDTO.from(getEntity(id));
    }

    @Transactional
    public MemberResponseDTO create(MemberRequestDTO request) {
        Member member = new Member();
        // padrões explícitos: valem mesmo que a entidade não tenha valor inicial
        member.setCargo(Cargo.MEMBRO);
        member.setStatus(Status.PENDING);
        apply(member, request);
        return MemberResponseDTO.from(repository.save(member));
    }

    @Transactional
    public MemberResponseDTO update(Long id, MemberRequestDTO request) {
        Member member = getEntity(id);
        apply(member, request);
        return MemberResponseDTO.from(repository.save(member));
    }

    /** Soft delete: o registro continua no banco, só deixa de aparecer. */
    @Transactional
    public void delete(Long id) {
        Member member = getEntity(id);
        member.setDeletedAt(LocalDateTime.now());
    }

    /** Usado também pelo UserService. Não retorna membros excluídos. */
    @Transactional(readOnly = true)
    public Member getEntity(Long id) {
        return repository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membro não encontrado: " + id));
    }

    private void apply(Member member, MemberRequestDTO request) {
        member.setName(request.name());
        member.setBirthDate(request.birthDate());
        member.setPhotoUrl(request.photoUrl());
        member.setRg(request.rg());
        member.setBaptismDate(request.baptismDate());
        member.setFatherName(request.fatherName());
        member.setMotherName(request.motherName());
        member.setEmail(request.email());
        member.setPhone(request.phone());
        member.setChurch(churchService.getEntity(request.churchId()));

        // cargo e status são opcionais: se não vierem, mantém o valor atual
        // (no create, os padrões definidos acima: MEMBRO e PENDING)
        if (request.cargo() != null) {
            member.setCargo(request.cargo());
        }
        if (request.status() != null) {
            member.setStatus(request.status());
        }
    }
}