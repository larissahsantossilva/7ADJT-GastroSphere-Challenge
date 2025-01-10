package br.com.fiap.gastrosphere.controllers.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import br.com.fiap.gastrosphere.dtos.ResourceNotFoundDTO;
import br.com.fiap.gastrosphere.dtos.ValidationErrorDTO;
import br.com.fiap.gastrosphere.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ResourceNotFoundDTO> handlerResourceNotFoundException(ResourceNotFoundException e) {
		var status = HttpStatus.NOT_FOUND;
		return ResponseEntity.status(status.value()).body(new ResourceNotFoundDTO(e.getMessage(), status.value()));
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationErrorDTO> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		var status = HttpStatus.BAD_REQUEST;
		List<String> errors = new ArrayList<>();
		for (var error: e.getBindingResult().getFieldErrors()) {
			errors.add(error.getField() + ": " + error.getDefaultMessage());
		}
		return ResponseEntity.status(status.value()).body(new ValidationErrorDTO(errors, status.value()));
	} 
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResourceNotFoundDTO> handleMethodArgumentMismatchException(MethodArgumentTypeMismatchException ex) {
        if (ex.getName().equals("id") && ex.getRequiredType().equals(UUID.class)) {
            return ResponseEntity.badRequest().body(new ResourceNotFoundDTO("ID de usuário inválido", 400));
        }
        return ResponseEntity.internalServerError().body(new ResourceNotFoundDTO("Erro interno do servidor", 500));
    }
}
