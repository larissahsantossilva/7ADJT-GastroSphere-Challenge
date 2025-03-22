package br.com.fiap.gastrosphere.core.domain.exception;

public class UnprocessableEntityException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UnprocessableEntityException(String errorMessage){
		super(errorMessage);
	}
}
