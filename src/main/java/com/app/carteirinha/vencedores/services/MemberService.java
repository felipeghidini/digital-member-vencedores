package com.app.carteirinha.vencedores.services;

import com.app.carteirinha.vencedores.entities.Member;
import com.app.carteirinha.vencedores.enums.Cargo;
import com.app.carteirinha.vencedores.enums.Status;
import com.app.carteirinha.vencedores.exceptions.ResourceNotFoundException;
import com.app.carteirinha.vencedores.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member não encontrado"));
    }

    public List<Member> getPendingMembers() {
        return memberRepository.findByStatus(Status.PENDING);
    }

    public Member approveMember(Long id) {
        Member member = getMemberById(id);
        member.setStatus(Status.ACTIVE);
        return memberRepository.save(member);
    }

    public Member inactivateMember(Long id) {
        Member member = getMemberById(id);
        member.setStatus(Status.INACTIVE);
        return memberRepository.save(member);
    }

    public Member updateMemberCargo(Long id, Cargo cargo) {
        Member member = getMemberById(id);
        member.setCargo(cargo);
        return memberRepository.save(member);
    }
}

