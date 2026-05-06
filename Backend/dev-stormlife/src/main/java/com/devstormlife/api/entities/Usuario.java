package com.devstormlife.api.entities;

import com.devstormlife.api.validations.annotations.SenhaValida;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    @Column(nullable = false, unique = true)
    private String email;

    @SenhaValida
    @NotBlank(message = "A Senha é obrigatóra")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    @Column(nullable = false)
    private String senha;

    @NotBlank(message = "Tipo de usuário é obrigatório")
    @Column(nullable = false)
    private String tipo;

    public Usuario() {

    }

    public Usuario(String email, String senha, String tipo) {
        this.email = email;
        this.senha = senha;
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}

