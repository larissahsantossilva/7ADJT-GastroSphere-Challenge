package br.com.fiap.gastrosphere.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.fiap.gastrosphere.core.infra.model.MenuItemModel;
import br.com.fiap.gastrosphere.core.infra.model.MenuModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MenuModelTest {

    private MenuModel menu;

    @BeforeEach
    void setUp() {
        menu = new MenuModel();
    }

    @Test
    void testGettersAndSetters() {
        UUID id = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        String name = "Menu Especial";
        LocalDate createdAt = LocalDate.of(2023, 1, 1);
        LocalDate lastModifiedAt = LocalDate.of(2023, 1, 2);
        List<MenuItemModel> itemsMenu = new ArrayList<>();

        menu.setId(id);
        menu.setRestaurantId(restaurantId);
        menu.setName(name);
        menu.setCreatedAt(createdAt);
        menu.setLastModifiedAt(lastModifiedAt);
        menu.setItemsMenu(itemsMenu);

        assertEquals(id, menu.getId());
        assertEquals(restaurantId, menu.getRestaurantId());
        assertEquals(name, menu.getName());
        assertEquals(createdAt, menu.getCreatedAt());
        assertEquals(lastModifiedAt, menu.getLastModifiedAt());
        assertEquals(itemsMenu, menu.getItemsMenu());
    }

    @Test
    void testDefaultValues() {
        assertNotNull(menu.getCreatedAt());
        assertNotNull(menu.getLastModifiedAt());
        assertEquals(LocalDate.now(), menu.getCreatedAt());
        assertEquals(LocalDate.now(), menu.getLastModifiedAt());
        assertNotNull(menu.getItemsMenu());
        assertTrue(menu.getItemsMenu().isEmpty());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();

        menu.setId(id);
        menu.setRestaurantId(restaurantId);
        menu.setName("Menu Teste");

        MenuModel sameMenu = new MenuModel();
        sameMenu.setId(id);
        sameMenu.setRestaurantId(restaurantId);
        sameMenu.setName("Menu Teste");

        MenuModel differentMenu = new MenuModel();
        differentMenu.setId(UUID.randomUUID());
        differentMenu.setRestaurantId(UUID.randomUUID());
        differentMenu.setName("Outro Menu");

        assertEquals(menu, sameMenu);
        assertEquals(menu.hashCode(), sameMenu.hashCode());
        assertNotEquals(menu, differentMenu);
    }

    @Test
    void testNoArgsConstructor() {
        MenuModel newMenu = new MenuModel();
        assertNotNull(newMenu.getCreatedAt());
        assertNotNull(newMenu.getLastModifiedAt());
        assertNotNull(newMenu.getItemsMenu());
        assertTrue(newMenu.getItemsMenu().isEmpty());
        assertNull(newMenu.getId());
        assertNull(newMenu.getRestaurantId());
        assertNull(newMenu.getName());
    }

    @Test
    void testBuilder() {
        UUID id = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        List<MenuItemModel> itemsMenu = new ArrayList<>();

        MenuModel builtMenu = MenuModel.builder()
                .id(id)
                .restaurantId(restaurantId)
                .name("Menu Construído")
                .createdAt(LocalDate.of(2023, 1, 1))
                .lastModifiedAt(LocalDate.of(2023, 1, 2))
                .itemsMenu(itemsMenu)
                .build();

        assertEquals(id, builtMenu.getId());
        assertEquals(restaurantId, builtMenu.getRestaurantId());
        assertEquals("Menu Construído", builtMenu.getName());
        assertEquals(LocalDate.of(2023, 1, 1), builtMenu.getCreatedAt());
        assertEquals(LocalDate.of(2023, 1, 2), builtMenu.getLastModifiedAt());
        assertEquals(itemsMenu, builtMenu.getItemsMenu());
    }

    @Test
    void testAddItemToMenu() {
        MenuItemModel item = new MenuItemModel();
        List<MenuItemModel> itemsMenu = new ArrayList<>();
        itemsMenu.add(item);

        menu.setItemsMenu(itemsMenu);
        assertEquals(1, menu.getItemsMenu().size());
        assertTrue(menu.getItemsMenu().contains(item));
    }

    @Test
    void testRemoveItemFromMenu() {
        MenuItemModel item = new MenuItemModel();
        List<MenuItemModel> itemsMenu = new ArrayList<>();
        itemsMenu.add(item);

        menu.setItemsMenu(itemsMenu);
        menu.getItemsMenu().remove(item);
        assertTrue(menu.getItemsMenu().isEmpty());
    }
}
