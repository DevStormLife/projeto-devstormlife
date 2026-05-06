package com.devstormlife.api.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
@Entity
@Table(name = "cartoes_acesso")
public class CartaoAcesso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String codigoSerial;

    // Relacionamento: Muitos cartões podem pertencer a uma subestação
    @ManyToOne
    @JoinColumn(name = "subestacao_id")
    private Subestacao subestacao;

    @OneToOne(mappedBy = "cartaoAcesso")
    private Funcionario funcionario;


    // Getters e Setters...
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigoSerial() {
		return codigoSerial;
	}

	public void setCodigoSerial(String codigoSerial) {
		this.codigoSerial = codigoSerial;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public CartaoAcesso() {
		
	}
	
    public Subestacao getSubstacao() {
    	return subestacao; 
    	}
    
    public void setSubstacao(Subestacao substacao) {
    	this.subestacao = substacao; 
    	}
}