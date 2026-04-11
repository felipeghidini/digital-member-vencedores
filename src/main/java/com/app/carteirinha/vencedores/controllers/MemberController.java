package com.app.carteirinha.vencedores.controllers;

import com.app.carteirinha.vencedores.entities.Member;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/members")
public class MemberController {

    @GetMapping
    public List<Member> findAll() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date birthDate = sdf.parse("14/04/1989");
        Date baptismDate = sdf.parse("12/05/2011");

        Member member = new Member(1L, "Felipe", birthDate, "foto", "493369875", baptismDate, "Marco Antonio", "Maria Ivamar", null, null);
        return Arrays.asList(member);
    }
}
