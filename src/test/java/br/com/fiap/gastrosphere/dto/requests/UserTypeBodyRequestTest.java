package br.com.fiap.gastrosphere.dto.requests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.fiap.gastrosphere.core.application.dto.request.UserTypeBodyRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class UserTypeBodyRequestTest {

    private UserTypeBodyRequest userTypeBodyRequest;
    private Validator validator;

    @BeforeEach
    void setUp() {
        userTypeBodyRequest = new UserTypeBodyRequest();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testGettersAndSetters() {
        String name = "Administrador";

        userTypeBodyRequest.setName(name);

        assertEquals(name, userTypeBodyRequest.getName());
    }

    @Test
    void testValidationAnnotations() {
        userTypeBodyRequest.setName("");

        Set<ConstraintViolation<UserTypeBodyRequest>> violations = validator.validate(userTypeBodyRequest);

        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
    }

    @Test
    void testValidObject() {
        userTypeBodyRequest.setName("Administrador");

        Set<ConstraintViolation<UserTypeBodyRequest>> violations = validator.validate(userTypeBodyRequest);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testEqualsAndHashCode() {
        String name = "Administrador";

        userTypeBodyRequest.setName(name);

        UserTypeBodyRequest sameUserTypeBodyRequest = new UserTypeBodyRequest();
        sameUserTypeBodyRequest.setName(name);

        UserTypeBodyRequest differentUserTypeBodyRequest = new UserTypeBodyRequest();
        differentUserTypeBodyRequest.setName("Usuário");

        assertEquals(userTypeBodyRequest, sameUserTypeBodyRequest);
        assertEquals(userTypeBodyRequest.hashCode(), sameUserTypeBodyRequest.hashCode());
        assertNotEquals(userTypeBodyRequest, differentUserTypeBodyRequest);
    }

    @Test
    void testToString() {
        userTypeBodyRequest.setName("Administrador");

        String toStringResult = userTypeBodyRequest.toString();

        assertTrue(toStringResult.contains("Administrador"));
        assertTrue(toStringResult.contains("name="));
    }
}
