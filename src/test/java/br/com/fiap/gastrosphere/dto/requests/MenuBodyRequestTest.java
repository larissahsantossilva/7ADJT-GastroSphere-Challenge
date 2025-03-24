package br.com.fiap.gastrosphere.dto.requests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.fiap.gastrosphere.core.application.dto.request.MenuBodyRequest;
import br.com.fiap.gastrosphere.core.application.dto.request.MenuItemBodyRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class MenuBodyRequestTest {

    private MenuBodyRequest menuBodyRequest;
    private Validator validator;

    @BeforeEach
    void setUp() {
        menuBodyRequest = new MenuBodyRequest();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testGettersAndSetters() {
        UUID restaurantId = UUID.randomUUID();
        String name = "Menu Teste";
        List<MenuItemBodyRequest> itemsMenu = List.of(new MenuItemBodyRequest());

        menuBodyRequest.setRestaurantId(restaurantId);
        menuBodyRequest.setName(name);
        menuBodyRequest.setItemsMenu(itemsMenu);

        assertEquals(restaurantId, menuBodyRequest.getRestaurantId());
        assertEquals(name, menuBodyRequest.getName());
        assertEquals(itemsMenu, menuBodyRequest.getItemsMenu());
    }

    @Test
    void testValidationAnnotations() {
        menuBodyRequest.setRestaurantId(null);
        menuBodyRequest.setName(null);
        menuBodyRequest.setItemsMenu(null);

        Set<ConstraintViolation<MenuBodyRequest>> violations = validator.validate(menuBodyRequest);

        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
    }

    @Test
    void testValidObject() {
        menuBodyRequest.setRestaurantId(UUID.randomUUID());
        menuBodyRequest.setName("Menu Teste");
        menuBodyRequest.setItemsMenu(List.of(new MenuItemBodyRequest()));
        Set<ConstraintViolation<MenuBodyRequest>> violations = validator.validate(menuBodyRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID restaurantId = UUID.randomUUID();
        String name = "Menu Teste";

        menuBodyRequest.setRestaurantId(restaurantId);
        menuBodyRequest.setName(name);

        MenuBodyRequest sameMenuBodyRequest = new MenuBodyRequest();
        sameMenuBodyRequest.setRestaurantId(restaurantId);
        sameMenuBodyRequest.setName(name);

        MenuBodyRequest differentMenuBodyRequest = new MenuBodyRequest();
        differentMenuBodyRequest.setRestaurantId(UUID.randomUUID());
        differentMenuBodyRequest.setName("Outro Menu");

        assertEquals(menuBodyRequest, sameMenuBodyRequest);
        assertEquals(menuBodyRequest.hashCode(), sameMenuBodyRequest.hashCode());
        assertNotEquals(menuBodyRequest, differentMenuBodyRequest);
    }

    @Test
    void testToString() {
        menuBodyRequest.setRestaurantId(UUID.randomUUID());
        menuBodyRequest.setName("Menu Teste");

        String toStringResult = menuBodyRequest.toString();

        assertTrue(toStringResult.contains("Menu Teste"));
        assertTrue(toStringResult.contains("restaurantId="));
    }
}
