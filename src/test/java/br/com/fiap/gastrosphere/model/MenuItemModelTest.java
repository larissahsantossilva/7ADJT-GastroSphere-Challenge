package br.com.fiap.gastrosphere.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.fiap.gastrosphere.core.infra.model.MenuItemModel;
import br.com.fiap.gastrosphere.core.infra.model.MenuModel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MenuItemModelTest {

    private MenuItemModel menuItem;

    @BeforeEach
    void setUp() {
        menuItem = new MenuItemModel();
    }

    @Test
    void testGettersAndSetters() {
        UUID id = UUID.randomUUID();
        MenuModel menu = new MenuModel();
        String description = "Pizza Margherita";
        BigDecimal price = BigDecimal.valueOf(39.99);
        String image = "pizza.jpg";
        Boolean isAvailable = true;
        LocalDate createdAt = LocalDate.of(2023, 1, 1);
        LocalDate lastModifiedAt = LocalDate.of(2023, 1, 2);

        menuItem.setId(id);
        menuItem.setMenu(menu);
        menuItem.setDescription(description);
        menuItem.setPrice(price);
        menuItem.setImage(image);
        menuItem.setIsAvailable(isAvailable);
        menuItem.setCreatedAt(createdAt);
        menuItem.setLastModifiedAt(lastModifiedAt);

        assertEquals(id, menuItem.getId());
        assertEquals(menu, menuItem.getMenu());
        assertEquals(description, menuItem.getDescription());
        assertEquals(price, menuItem.getPrice());
        assertEquals(image, menuItem.getImage());
        assertEquals(isAvailable, menuItem.getIsAvailable());
        assertEquals(createdAt, menuItem.getCreatedAt());
        assertEquals(lastModifiedAt, menuItem.getLastModifiedAt());
    }

    @Test
    void testDefaultValues() {
        assertNotNull(menuItem.getCreatedAt());
        assertNotNull(menuItem.getLastModifiedAt());
        assertEquals(LocalDate.now(), menuItem.getCreatedAt());
        assertEquals(LocalDate.now(), menuItem.getLastModifiedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        MenuModel menu = new MenuModel();

        menuItem.setId(id);
        menuItem.setMenu(menu);
        menuItem.setDescription("Pizza Margherita");
        menuItem.setPrice(BigDecimal.valueOf(39.99));
        menuItem.setIsAvailable(true);

        MenuItemModel sameMenuItem = new MenuItemModel();
        sameMenuItem.setId(id);
        sameMenuItem.setMenu(menu);
        sameMenuItem.setDescription("Pizza Margherita");
        sameMenuItem.setPrice(BigDecimal.valueOf(39.99));
        sameMenuItem.setIsAvailable(true);

        MenuItemModel differentMenuItem = new MenuItemModel();
        differentMenuItem.setId(UUID.randomUUID());
        differentMenuItem.setMenu(new MenuModel());
        differentMenuItem.setDescription("Calzone");
        differentMenuItem.setPrice(BigDecimal.valueOf(29.99));
        differentMenuItem.setIsAvailable(false);

        // Validações
        assertEquals(menuItem, sameMenuItem);
        assertEquals(menuItem.hashCode(), sameMenuItem.hashCode());
        assertNotEquals(menuItem, differentMenuItem);
    }

    @Test
    void testNoArgsConstructor() {
        MenuItemModel newMenuItem = new MenuItemModel();
        assertNotNull(newMenuItem.getCreatedAt());
        assertNotNull(newMenuItem.getLastModifiedAt());
        assertNull(newMenuItem.getId());
        assertNull(newMenuItem.getMenu());
        assertNull(newMenuItem.getDescription());
        assertNull(newMenuItem.getPrice());
        assertNull(newMenuItem.getImage());
        assertNull(newMenuItem.getIsAvailable());
    }

    @Test
    void testBuilder() {
        UUID id = UUID.randomUUID();
        MenuModel menu = new MenuModel();
        MenuItemModel builtMenuItem = MenuItemModel.builder()
                .id(id)
                .menu(menu)
                .description("Lasanha")
                .price(BigDecimal.valueOf(45.90))
                .image("lasanha.jpg")
                .isAvailable(true)
                .createdAt(LocalDate.of(2023, 1, 1))
                .lastModifiedAt(LocalDate.of(2023, 1, 2))
                .build();

        assertEquals(id, builtMenuItem.getId());
        assertEquals(menu, builtMenuItem.getMenu());
        assertEquals("Lasanha", builtMenuItem.getDescription());
        assertEquals(BigDecimal.valueOf(45.90), builtMenuItem.getPrice());
        assertEquals("lasanha.jpg", builtMenuItem.getImage());
        assertTrue(builtMenuItem.getIsAvailable());
        assertEquals(LocalDate.of(2023, 1, 1), builtMenuItem.getCreatedAt());
        assertEquals(LocalDate.of(2023, 1, 2), builtMenuItem.getLastModifiedAt());
    }
}
