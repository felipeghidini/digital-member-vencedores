package com.app.carteirinha.vencedores.controllers;

import com.app.carteirinha.vencedores.dtos.MemberRequestDTO;
import com.app.carteirinha.vencedores.dtos.MemberResponseDTO;
import com.app.carteirinha.vencedores.entities.Member;
import com.app.carteirinha.vencedores.enums.Cargo;
import com.app.carteirinha.vencedores.services.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService service;

    /** GET /members  ou  GET /members?churchId=1 */
    @GetMapping
    public List<MemberResponseDTO> list(@RequestParam(required = false) Long churchId) {
        return service.findAll(churchId);
    }

    @GetMapping("/{id}")
    public MemberResponseDTO get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<MemberResponseDTO> create(@Valid @RequestBody MemberRequestDTO request) {
        MemberResponseDTO created = service.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public MemberResponseDTO update(@PathVariable Long id, @Valid @RequestBody MemberRequestDTO request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}



