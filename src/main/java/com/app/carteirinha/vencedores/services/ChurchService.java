package com.app.carteirinha.vencedores.services;



import com.app.carteirinha.vencedores.dtos.ChurchRequestDTO;
import com.app.carteirinha.vencedores.dtos.ChurchResponseDTO;
import com.app.carteirinha.vencedores.entities.Church;
import com.app.carteirinha.vencedores.exceptions.ResourceNotFoundException;
import com.app.carteirinha.vencedores.repositories.ChurchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChurchService {

    private final ChurchRepository repository;

    @Transactional(readOnly = true)
    public List<ChurchResponseDTO> findAll() {
        return repository.findAll().stream().map(ChurchResponseDTO::from).toList();
    }

    @Transactional(readOnly = true)
    public ChurchResponseDTO findById(Long id) {
        return ChurchResponseDTO.from(getEntity(id));
    }

    @Transactional
    public ChurchResponseDTO create(ChurchRequestDTO request) {
        Church church = new Church();
        apply(church, request);
        return ChurchResponseDTO.from(repository.save(church));
    }

    @Transactional
    public ChurchResponseDTO update(Long id, ChurchRequestDTO request) {
        Church church = getEntity(id);
        apply(church, request);
        return ChurchResponseDTO.from(repository.save(church));
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(getEntity(id));
        repository.flush(); // força o erro de FK (409) aqui, e não só no commit
    }

    /** Usado também por outros services (Member, User) que precisam da entidade. */
    @Transactional(readOnly = true)
    public Church getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Igreja não encontrada: " + id));
    }

    private void apply(Church church, ChurchRequestDTO request) {
        church.setName(request.name());
        church.setCity(request.city());
        church.setAddress(request.address());
    }
}

