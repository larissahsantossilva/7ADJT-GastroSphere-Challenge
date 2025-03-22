package br.com.fiap.gastrosphere.core.domain.exception.user;

public class UserNotFoundException extends Exception {

    public UserNotFoundException(String message) {
        super(message);
    }

}
