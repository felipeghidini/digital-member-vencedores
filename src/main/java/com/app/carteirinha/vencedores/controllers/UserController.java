package com.app.carteirinha.vencedores.controllers;

import com.app.carteirinha.vencedores.entities.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Arrays;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @GetMapping
    public List<User> findAll() {
        User user = new User(1L, "123456789", "132345");
        User user2 = new User(2L,"123456789", "132345");
        User user3 = new User(3L, "123456789", "132345");
        return Arrays.asList(user, user2, user3);
    }
}
