package com.devstormlife.api.entities;

import org.hibernate.validator.constraints.br.CPF;

import com.devstormlife.api.validations.annotations.SenhaValida;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "funcionarios")
public class Funcionario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String nome;

	@NotBlank
	@Email
	@Column(unique = true)
	private String email;

	@SenhaValida
	@NotBlank(message = "A Senha é obrigatóra")
	@Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
	@Column(nullable = false)
	private String senha;

	@NotBlank
	@CPF
	@Column(unique = true)
	private String cpf;

	@NotBlank
	private String cargo;

	// Ligação com a outra entidade
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cartao_acesso_id", referencedColumnName = "id")
	private CartaoAcesso cartaoAcesso;

	// Ligação à Subestação
	@NotNull
	@ManyToOne
	@JoinColumn(name = "fk_subestacao", nullable = false)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Subestacao subestacao;

	// Getters e Setters
	public Funcionario() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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
		this.email = senha;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public CartaoAcesso getCartaoAcesso() {
		return cartaoAcesso;
	}

	public void setCartaoAcesso(CartaoAcesso cartaoAcesso) {
		this.cartaoAcesso = cartaoAcesso;
	}

	public Subestacao getSubestacao() {
		return subestacao;
	}

	public void setSubestacao(Subestacao subestacao) {
		this.subestacao = subestacao;
	}
}
