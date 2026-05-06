package com.devstormlife.api.validations.validators;

import com.devstormlife.api.validations.annotations.SenhaValida;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SenhaValidaValidator implements ConstraintValidator<SenhaValida, String> {
	@Override
	public boolean isValid(String valor, ConstraintValidatorContext context) {

		if (valor == null) {
			return true;
		}

		String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*\\W).{8,}$";

		return valor.matches(regex);

	}
}
