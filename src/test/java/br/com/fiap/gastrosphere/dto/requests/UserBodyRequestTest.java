package br.com.fiap.gastrosphere.dto.requests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.fiap.gastrosphere.core.application.dto.request.AddressBodyRequest;
import br.com.fiap.gastrosphere.core.application.dto.request.UserBodyRequest;
import br.com.fiap.gastrosphere.core.application.dto.request.UserTypeBodyRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class UserBodyRequestTest {

    private UserBodyRequest userBodyRequest;
    private Validator validator;

    @BeforeEach
    void setUp() {
        userBodyRequest = new UserBodyRequest();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testGettersAndSetters() {
        String name = "João da Silva";
        String email = "joao.silva@example.com";
        String login = "joaosilva";
        String password = "senha12345";
        String document = "12345678900";
        UserTypeBodyRequest userType = new UserTypeBodyRequest();
        AddressBodyRequest address = new AddressBodyRequest();

        userBodyRequest.setName(name);
        userBodyRequest.setEmail(email);
        userBodyRequest.setLogin(login);
        userBodyRequest.setPassword(password);
        userBodyRequest.setDocument(document);
        userBodyRequest.setUserType(userType);
        userBodyRequest.setAddress(address);

        assertEquals(name, userBodyRequest.getName());
        assertEquals(email, userBodyRequest.getEmail());
        assertEquals(login, userBodyRequest.getLogin());
        assertEquals(password, userBodyRequest.getPassword());
        assertEquals(document, userBodyRequest.getDocument());
        assertEquals(userType, userBodyRequest.getUserType());
        assertEquals(address, userBodyRequest.getAddress());
    }

    @Test
    void testValidationAnnotations() {
        userBodyRequest.setName(null);
        userBodyRequest.setEmail(null);
        userBodyRequest.setLogin(null);
        userBodyRequest.setPassword(null);
        userBodyRequest.setDocument(null);
        userBodyRequest.setUserType(null);
        userBodyRequest.setAddress(null);

        Set<ConstraintViolation<UserBodyRequest>> violations = validator.validate(userBodyRequest);

        assertFalse(violations.isEmpty());
        assertEquals(12, violations.size());
    }

    @Test
    void testValidObject() {
        userBodyRequest.setName("João da Silva");
        userBodyRequest.setEmail("joao.silva@example.com");
        userBodyRequest.setLogin("joaosilva");
        userBodyRequest.setPassword("senha12345");
        userBodyRequest.setDocument("12345678900");
        userBodyRequest.setUserType(new UserTypeBodyRequest());
        userBodyRequest.setAddress(new AddressBodyRequest());

        Set<ConstraintViolation<UserBodyRequest>> violations = validator.validate(userBodyRequest);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testEqualsAndHashCode() {
        String name = "João da Silva";
        String email = "joao.silva@example.com";

        userBodyRequest.setName(name);
        userBodyRequest.setEmail(email);

        UserBodyRequest sameUserBodyRequest = new UserBodyRequest();
        sameUserBodyRequest.setName(name);
        sameUserBodyRequest.setEmail(email);

        UserBodyRequest differentUserBodyRequest = new UserBodyRequest();
        differentUserBodyRequest.setName("Maria dos Santos");
        differentUserBodyRequest.setEmail("maria.santos@example.com");

        assertEquals(userBodyRequest, sameUserBodyRequest);
        assertEquals(userBodyRequest.hashCode(), sameUserBodyRequest.hashCode());
        assertNotEquals(userBodyRequest, differentUserBodyRequest);
    }

    @Test
    void testToString() {
        userBodyRequest.setName("João da Silva");
        userBodyRequest.setEmail("joao.silva@example.com");

        String toStringResult = userBodyRequest.toString();

        assertTrue(toStringResult.contains("João da Silva"));
        assertTrue(toStringResult.contains("joao.silva@example.com"));
    }
}
