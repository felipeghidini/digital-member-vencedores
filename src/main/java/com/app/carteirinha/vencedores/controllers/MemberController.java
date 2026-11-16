package com.app.carteirinha.vencedores.controllers;

import com.app.carteirinha.vencedores.entities.Member;
import com.app.carteirinha.vencedores.enums.Cargo;
import com.app.carteirinha.vencedores.services.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Member>> getPendingMembers() {
        return ResponseEntity.ok(memberService.getPendingMembers());
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<Member> approveMember(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.approveMember(id));
    }

    @PatchMapping("/{id}/inactive")
    public ResponseEntity<Member> inactivateMember(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.inactivateMember(id));
    }

    @PatchMapping("/{id}/cargo")
    public ResponseEntity<Member> updateMemberCargo(@PathVariable Long id, @RequestParam Cargo cargo) {
        return ResponseEntity.ok(memberService.updateMemberCargo(id, cargo));
    }
}



