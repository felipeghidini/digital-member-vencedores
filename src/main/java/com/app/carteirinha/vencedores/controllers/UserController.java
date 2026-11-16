package com.app.carteirinha.vencedores.controllers;

import com.app.carteirinha.vencedores.entities.User;
import com.app.carteirinha.vencedores.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return ResponseEntity.ok(user);
    }

    @GetMapping("/me")
    public ResponseEntity<String> getCurrentUser() {
        // Temporariamente retornar mensagem
        // Quando Spring Security estiver funcionando, retornar dados do usuário
        return ResponseEntity.ok("Endpoint para usuário autenticado. Configure Spring Security para usar este endpoint.");
    }
}




