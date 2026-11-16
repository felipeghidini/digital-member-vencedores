package com.app.carteirinha.vencedores.services;

import com.app.carteirinha.vencedores.entities.User;
import com.app.carteirinha.vencedores.exceptions.ResourceNotFoundException;
import com.app.carteirinha.vencedores.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    public User getUserByCpf(String cpf) {
        return userRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }
}

