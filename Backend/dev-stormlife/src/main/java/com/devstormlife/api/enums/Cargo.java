package com.devstormlife.api.enums;

public enum Cargo {
	OPERADOR("ROLE_OPERADOR"),
	ENGENHEIRO("ROLE_ENGENHEIRO"),
	ELETROTECNICO("ROLE_ELETROTECNICO"),
	SUPERVISOR("ROLE_SUPERVISOR"),
	CHEFE("ROLE_CHEFE");
	
	private final String role;
	
	Cargo(String role) {
		this.role = role;
	}
	
	public String getRole() {
		return role;
	}
	
}
