package com.devstormlife.api.entities;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "subestacoes")
public class Subestacao {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "nome_estacao")
    private String nome;
    
    @Pattern(
    		regexp="^SUBEST-\\d{4}-\\d{2}$",
    		message="Código da subestação deve seguir o seguinte padrão: SUBEST-1234-56.")
    @NotBlank(message = "O código da estação é obrigatório.")
    @Size(min=12, max=15, message="O código da estação deve ter entre 12 e 15 dígitos.")
    private String codigoSubestacao;
    
    @NotBlank
    private String localizacao;

    // Uma subestação pode ter vários cartões vinculados à ela.
    @OneToMany(mappedBy = "subestacao")
    @JsonIgnore
    private List<CartaoAcesso> cartoes;
    
    // Uma subestação deve ter vários funcionários vinculados à ela.
    @OneToMany(mappedBy = "subestacao")
    @JsonIgnore
    private List<Funcionario> funcionarios;

    public Subestacao() {}

    // Getters e Setters...
    
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
	
	public String getCodigoSubestacao() {
		return codigoSubestacao;
	}
	
	public void setCodigoSubestacao(String codigoSubestacao) {
		this.codigoSubestacao = codigoSubestacao;
	}

	public String getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}

	public List<CartaoAcesso> getCartoes() {
		return cartoes;
	}

	public void setCartoes(List<CartaoAcesso> cartoes) {
		this.cartoes = cartoes;
	}
    
	public List<Funcionario> getFuncionarios() {
		return funcionarios;
	}
	
	public void setFuncionarios(List<Funcionario> funcionarios) {
		this.funcionarios = funcionarios;
	}
}

