package br.com.fiap.gastrosphere.integration.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import br.com.fiap.gastrosphere.core.infra.repository.MenuItemRepository;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class MenuItemIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MenuItemRepository menuItemRepository;

    private static UUID createdMenuItemId;
    private static final UUID menuId = UUID.fromString("00000000-0000-0000-0000-000000000005");

    @Test
    @Order(1)
    void shouldCreateMenuItem() throws Exception {
        String json = """
        {
          "description": "Novo Item de Menu",
          "price": 49.90,
          "image": "item.jpg",
          "isAvailable": true
        }
        """;
        System.out.println("Menu ID usado: " + menuId);

        var result = mockMvc.perform(post("/api/v1/menu/{menu_id}/items", menuId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNotEmpty())
                .andReturn();
		String id = result.getResponse().getContentAsString().replace("\"", "");
		createdMenuItemId = UUID.fromString(id);
		assertThat(createdMenuItemId).isNotNull();
    }

    @Test
    @Order(2)
    void shouldGetMenuItemById() throws Exception {
        mockMvc.perform(get("/api/v1/menu/{menu_id}/items/{menu_item_id}", menuId, createdMenuItemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdMenuItemId.toString()));
    }

    @Test
    @Order(3)
    void shouldReturnNotFoundWhenMenuItemDoesNotExist() throws Exception {
        UUID fakeId = UUID.randomUUID();
        mockMvc.perform(get("/api/v1/menu/{menu_id}/items/{menu_item_id}", menuId, fakeId))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(4)
    void shouldListAllMenuItemsByMenuId() throws Exception {
        mockMvc.perform(get("/api/v1/menu/{menu_id}/items", menuId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(5)
    void shouldUpdateMenuItem() throws Exception {
        String json = """
        {
          "description": "Item Atualizado",
          "price": 59.90,
          "isAvailable": false,
          "image": "updated-item.jpg"
        }
        """;

        mockMvc.perform(put("/api/v1/menu/{menu_id}/items/{menu_item_id}", menuId, createdMenuItemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    void shouldDeleteMenuItem() throws Exception {
        mockMvc.perform(delete("/api/v1/menu/{menu_id}/items/{menu_item_id}", menuId, createdMenuItemId))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(7)
    void shouldReturnNotFoundOnDeleteWhenMenuItemDoesNotExist() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        while (menuItemRepository.existsById(nonExistentId)) {
            nonExistentId = UUID.randomUUID();
        }

        mockMvc.perform(delete("/api/v1/menu/{menu_id}/items/{menu_item_id}", menuId, nonExistentId))
                .andExpect(status().isNotFound());
    }
}
