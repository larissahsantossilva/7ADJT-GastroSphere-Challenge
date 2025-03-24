package br.com.fiap.gastrosphere.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.fiap.gastrosphere.core.infra.model.RestaurantTypeModel;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantTypeModelTest {

    private RestaurantTypeModel restaurantType;

    @BeforeEach
    void setUp() {
        restaurantType = new RestaurantTypeModel();
    }

    @Test
    void testGettersAndSetters() {
        UUID id = UUID.randomUUID();
        String name = "Italiana";
        LocalDate createdAt = LocalDate.of(2023, 1, 1);
        LocalDate lastModifiedAt = LocalDate.of(2023, 1, 2);

        restaurantType.setId(id);
        restaurantType.setName(name);
        restaurantType.setCreatedAt(createdAt);
        restaurantType.setLastModifiedAt(lastModifiedAt);

        assertEquals(id, restaurantType.getId());
        assertEquals(name, restaurantType.getName());
        assertEquals(createdAt, restaurantType.getCreatedAt());
        assertEquals(lastModifiedAt, restaurantType.getLastModifiedAt());
    }

    @Test
    void testDefaultValues() {
        assertNotNull(restaurantType.getCreatedAt());
        assertNotNull(restaurantType.getLastModifiedAt());
        assertEquals(LocalDate.now(), restaurantType.getCreatedAt());
        assertEquals(LocalDate.now(), restaurantType.getLastModifiedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        String name = "Brasileira";

        restaurantType.setId(id);
        restaurantType.setName(name);

        RestaurantTypeModel sameRestaurantType = new RestaurantTypeModel();
        sameRestaurantType.setId(id);
        sameRestaurantType.setName(name);

        RestaurantTypeModel differentRestaurantType = new RestaurantTypeModel();
        differentRestaurantType.setId(UUID.randomUUID());
        differentRestaurantType.setName("Japonesa");

        assertEquals(restaurantType, sameRestaurantType);
        assertEquals(restaurantType.hashCode(), sameRestaurantType.hashCode());
        assertNotEquals(restaurantType, differentRestaurantType);
    }

    @Test
    void testNoArgsConstructor() {
        RestaurantTypeModel newRestaurantType = new RestaurantTypeModel();
        assertNotNull(newRestaurantType.getCreatedAt());
        assertNotNull(newRestaurantType.getLastModifiedAt());
        assertNull(newRestaurantType.getId());
        assertNull(newRestaurantType.getName());
    }

    @Test
    void testToString() {
        restaurantType.setId(UUID.randomUUID());
        restaurantType.setName("Mexicana");
        String toStringResult = restaurantType.toString();
        assertTrue(toStringResult.contains("Mexicana"));
        assertTrue(toStringResult.contains("id="));
    }
}
