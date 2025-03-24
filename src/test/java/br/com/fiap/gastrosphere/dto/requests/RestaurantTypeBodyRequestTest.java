package br.com.fiap.gastrosphere.dto.requests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.fiap.gastrosphere.core.application.dto.request.RestaurantTypeBodyRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class RestaurantTypeBodyRequestTest {

    private RestaurantTypeBodyRequest restaurantTypeBodyRequest;
    private Validator validator;

    @BeforeEach
    void setUp() {
        restaurantTypeBodyRequest = new RestaurantTypeBodyRequest();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testGettersAndSetters() {
        String name = "Brasileira";

        restaurantTypeBodyRequest.setName(name);

        assertEquals(name, restaurantTypeBodyRequest.getName());
    }

    @Test
    void testValidationAnnotations() {
        restaurantTypeBodyRequest.setName("");

        Set<ConstraintViolation<RestaurantTypeBodyRequest>> violations = validator.validate(restaurantTypeBodyRequest);

        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
    }

    @Test
    void testValidObject() {
        restaurantTypeBodyRequest.setName("Brasileira");

        Set<ConstraintViolation<RestaurantTypeBodyRequest>> violations = validator.validate(restaurantTypeBodyRequest);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testEqualsAndHashCode() {
        String name = "Brasileira";

        restaurantTypeBodyRequest.setName(name);

        RestaurantTypeBodyRequest sameRestaurantTypeBodyRequest = new RestaurantTypeBodyRequest();
        sameRestaurantTypeBodyRequest.setName(name);

        RestaurantTypeBodyRequest differentRestaurantTypeBodyRequest = new RestaurantTypeBodyRequest();
        differentRestaurantTypeBodyRequest.setName("Japonesa");

        assertEquals(restaurantTypeBodyRequest, sameRestaurantTypeBodyRequest);
        assertEquals(restaurantTypeBodyRequest.hashCode(), sameRestaurantTypeBodyRequest.hashCode());
        assertNotEquals(restaurantTypeBodyRequest, differentRestaurantTypeBodyRequest);
    }

    @Test
    void testToString() {
        restaurantTypeBodyRequest.setName("Brasileira");

        String toStringResult = restaurantTypeBodyRequest.toString();

        assertTrue(toStringResult.contains("Brasileira"));
        assertTrue(toStringResult.contains("name="));
    }
}
