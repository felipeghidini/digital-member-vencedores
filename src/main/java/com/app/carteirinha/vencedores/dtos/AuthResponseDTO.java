package com.app.carteirinha.vencedores.dtos;

public class AuthResponseDTO {
    private String token;
    private String type = "Bearer";
    private Long userId;
    private String cpf;
    private String role;

    public AuthResponseDTO() {
    }

    public AuthResponseDTO(String token, Long userId, String cpf, String role) {
        this.token = token;
        this.userId = userId;
        this.cpf = cpf;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

