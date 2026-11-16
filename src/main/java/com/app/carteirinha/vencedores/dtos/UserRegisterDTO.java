package com.app.carteirinha.vencedores.dtos;

public class UserRegisterDTO {
    private String cpf;
    private String password;
    private String name;

    public UserRegisterDTO() {
    }

    public UserRegisterDTO(String cpf, String password, String name) {
        this.cpf = cpf;
        this.password = password;
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

