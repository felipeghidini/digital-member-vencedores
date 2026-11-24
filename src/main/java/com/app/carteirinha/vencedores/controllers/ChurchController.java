package com.app.carteirinha.vencedores.controllers;

import com.app.carteirinha.vencedores.dtos.ChurchRequestDTO;
import com.app.carteirinha.vencedores.dtos.ChurchResponseDTO;
import com.app.carteirinha.vencedores.services.ChurchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/churches")
@RequiredArgsConstructor
public class ChurchController {

    private final ChurchService service;

    @GetMapping
    public List<ChurchResponseDTO> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ChurchResponseDTO get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<ChurchResponseDTO> create(@Valid @RequestBody ChurchRequestDTO request) {
        ChurchResponseDTO created = service.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ChurchResponseDTO update(@PathVariable Long id, @Valid @RequestBody ChurchRequestDTO request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}