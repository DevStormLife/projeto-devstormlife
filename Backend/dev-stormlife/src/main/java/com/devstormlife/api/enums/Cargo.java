package com.devstormlife.api.enums;

public enum Cargo {
	OPERADOR("Operador"),
	ENGENHEIRO("Engenheiro"),
	ELETROTECNICO("Eletrotecnico"),
	SUPERVISOR("Supervisor"),
	CHEFE("Chefe");
	
	private final String descricao;
	
	Cargo(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
}
