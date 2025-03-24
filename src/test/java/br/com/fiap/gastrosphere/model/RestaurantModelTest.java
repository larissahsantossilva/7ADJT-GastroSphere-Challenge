package br.com.fiap.gastrosphere.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.fiap.gastrosphere.core.infra.model.AddressModel;
import br.com.fiap.gastrosphere.core.infra.model.RestaurantModel;
import br.com.fiap.gastrosphere.core.infra.model.RestaurantTypeModel;
import br.com.fiap.gastrosphere.core.infra.model.UserModel;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantModelTest {

    private RestaurantModel restaurant;

    @BeforeEach
    void setUp() {
        restaurant = new RestaurantModel();
    }

    @Test
    void testGettersAndSetters() {
        UUID id = UUID.randomUUID();
        String name = "La Bella Pasta";
        UserModel user = new UserModel();
        AddressModel address = new AddressModel();
        RestaurantTypeModel restaurantType = new RestaurantTypeModel();
        LocalTime startedAt = LocalTime.of(9, 0);
        LocalTime finishedAt = LocalTime.of(22, 0);
        LocalDate createdAt = LocalDate.of(2023, 1, 1);
        LocalDate lastModifiedAt = LocalDate.of(2023, 1, 2);

        restaurant.setId(id);
        restaurant.setName(name);
        restaurant.setUser(user);
        restaurant.setAddress(address);
        restaurant.setRestaurantType(restaurantType);
        restaurant.setStartedAt(startedAt);
        restaurant.setFinishedAt(finishedAt);
        restaurant.setCreatedAt(createdAt);
        restaurant.setLastModifiedAt(lastModifiedAt);

        assertEquals(id, restaurant.getId());
        assertEquals(name, restaurant.getName());
        assertEquals(user, restaurant.getUser());
        assertEquals(address, restaurant.getAddress());
        assertEquals(restaurantType, restaurant.getRestaurantType());
        assertEquals(startedAt, restaurant.getStartedAt());
        assertEquals(finishedAt, restaurant.getFinishedAt());
        assertEquals(createdAt, restaurant.getCreatedAt());
        assertEquals(lastModifiedAt, restaurant.getLastModifiedAt());
    }

    @Test
    void testDefaultValues() {
        assertNotNull(restaurant.getCreatedAt());
        assertNotNull(restaurant.getLastModifiedAt());
        assertEquals(LocalDate.now(), restaurant.getCreatedAt());
        assertEquals(LocalDate.now(), restaurant.getLastModifiedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        String name = "La Bella Pasta";

        restaurant.setId(id);
        restaurant.setName(name);

        RestaurantModel sameRestaurant = new RestaurantModel();
        sameRestaurant.setId(id);
        sameRestaurant.setName(name);

        RestaurantModel differentRestaurant = new RestaurantModel();
        differentRestaurant.setId(UUID.randomUUID());
        differentRestaurant.setName("Outra Restaurante");

        assertEquals(restaurant, sameRestaurant);
        assertEquals(restaurant.hashCode(), sameRestaurant.hashCode());
        assertNotEquals(restaurant, differentRestaurant);
    }

    @Test
    void testNoArgsConstructor() {
        RestaurantModel newRestaurant = new RestaurantModel();
        assertNotNull(newRestaurant.getCreatedAt());
        assertNotNull(newRestaurant.getLastModifiedAt());
        assertNull(newRestaurant.getId());
        assertNull(newRestaurant.getName());
        assertNull(newRestaurant.getUser());
        assertNull(newRestaurant.getAddress());
        assertNull(newRestaurant.getRestaurantType());
        assertNull(newRestaurant.getStartedAt());
        assertNull(newRestaurant.getFinishedAt());
    }

    @Test
    void testToString() {
        restaurant.setId(UUID.randomUUID());
        restaurant.setName("La Bella Pasta");
        String toStringResult = restaurant.toString();
        assertTrue(toStringResult.contains("La Bella Pasta"));
        assertTrue(toStringResult.contains("id="));
    }
}
