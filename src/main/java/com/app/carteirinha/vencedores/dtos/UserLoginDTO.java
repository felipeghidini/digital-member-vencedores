package com.app.carteirinha.vencedores.dtos;

public class UserLoginDTO {
    private String cpf;
    private String password;

    public UserLoginDTO() {
    }

    public UserLoginDTO(String cpf, String password) {
        this.cpf = cpf;
        this.password = password;
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
}

