package br.com.fiap.gastrosphere.core.domain.exception.user;

public class UserAlreadyExistsException extends Exception {

    public UserAlreadyExistsException(String message) {
        super(message);
    }

}
