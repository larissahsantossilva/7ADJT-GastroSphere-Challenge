package br.com.fiap.gastrosphere.dto.requests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.fiap.gastrosphere.core.application.dto.request.MenuItemBodyRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class MenuItemBodyRequestTest {

    private MenuItemBodyRequest menuItemBodyRequest;
    private Validator validator;

    @BeforeEach
    void setUp() {
        menuItemBodyRequest = new MenuItemBodyRequest();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testGettersAndSetters() {
        UUID id = UUID.randomUUID();
        UUID menuId = UUID.randomUUID();
        String description = "Item de Teste";
        BigDecimal price = BigDecimal.valueOf(19.99);
        Boolean isAvailable = true;
        String image = "image.jpg";

        menuItemBodyRequest.setId(id);
        menuItemBodyRequest.setMenuId(menuId);
        menuItemBodyRequest.setDescription(description);
        menuItemBodyRequest.setPrice(price);
        menuItemBodyRequest.setIsAvailable(isAvailable);
        menuItemBodyRequest.setImage(image);

        assertEquals(id, menuItemBodyRequest.getId());
        assertEquals(menuId, menuItemBodyRequest.getMenuId());
        assertEquals(description, menuItemBodyRequest.getDescription());
        assertEquals(price, menuItemBodyRequest.getPrice());
        assertEquals(isAvailable, menuItemBodyRequest.getIsAvailable());
        assertEquals(image, menuItemBodyRequest.getImage());
    }

    @Test
    void testValidationAnnotations() {
        menuItemBodyRequest.setDescription("");
        menuItemBodyRequest.setPrice(BigDecimal.valueOf(-1));
        menuItemBodyRequest.setIsAvailable(null);

        Set<ConstraintViolation<MenuItemBodyRequest>> violations = validator.validate(menuItemBodyRequest);

        assertFalse(violations.isEmpty());
        assertEquals(5, violations.size());
    }

    @Test
    void testValidObject() {
        menuItemBodyRequest.setId(UUID.randomUUID());
        menuItemBodyRequest.setMenuId(UUID.randomUUID());
        menuItemBodyRequest.setDescription("Item válido");
        menuItemBodyRequest.setPrice(BigDecimal.valueOf(19.99));
        menuItemBodyRequest.setIsAvailable(true);
        menuItemBodyRequest.setImage("valid_image.jpg");

        Set<ConstraintViolation<MenuItemBodyRequest>> violations = validator.validate(menuItemBodyRequest);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        menuItemBodyRequest.setId(id);
        menuItemBodyRequest.setDescription("Item de Teste");

        MenuItemBodyRequest sameMenuItemBodyRequest = new MenuItemBodyRequest();
        sameMenuItemBodyRequest.setId(id);
        sameMenuItemBodyRequest.setDescription("Item de Teste");

        MenuItemBodyRequest differentMenuItemBodyRequest = new MenuItemBodyRequest();
        differentMenuItemBodyRequest.setId(UUID.randomUUID());
        differentMenuItemBodyRequest.setDescription("Outro Item");

        assertEquals(menuItemBodyRequest, sameMenuItemBodyRequest);
        assertEquals(menuItemBodyRequest.hashCode(), sameMenuItemBodyRequest.hashCode());
        assertNotEquals(menuItemBodyRequest, differentMenuItemBodyRequest);
    }

    @Test
    void testToString() {
        menuItemBodyRequest.setId(UUID.randomUUID());
        menuItemBodyRequest.setDescription("Item de Teste");

        String toStringResult = menuItemBodyRequest.toString();

        assertTrue(toStringResult.contains("Item de Teste"));
        assertTrue(toStringResult.contains("id="));
    }
}
