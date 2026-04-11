package com.app.carteirinha.vencedores.controllers;

import com.app.carteirinha.vencedores.entities.Member;
import com.app.carteirinha.vencedores.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/members")
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;

    @GetMapping
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @PostMapping
    public Member create(@RequestBody Member member) {
        return memberRepository.save(member);
    }
}


