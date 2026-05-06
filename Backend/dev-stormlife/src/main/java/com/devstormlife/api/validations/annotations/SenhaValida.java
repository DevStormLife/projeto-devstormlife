package com.devstormlife.api.validations.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.devstormlife.api.validations.validators.SenhaValidaValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = SenhaValidaValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SenhaValida {

	String message() default "Senha inválida, a senha deve ter pelo menos 8 caracteres, uma letra maiúscula, uma letra minúscula, um número e um caracter especial.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
