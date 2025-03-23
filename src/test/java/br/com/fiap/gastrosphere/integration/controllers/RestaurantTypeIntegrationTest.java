package br.com.fiap.gastrosphere.integration.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fiap.gastrosphere.core.infra.model.RestaurantTypeModel;
import br.com.fiap.gastrosphere.core.infra.repository.RestaurantTypeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
public class RestaurantTypeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestaurantTypeRepository restaurantTypeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private static UUID createdId;

    private RestaurantTypeModel createMockRestaurantType() {
        RestaurantTypeModel type = new RestaurantTypeModel();
        type.setName("Churrascaria");
        return restaurantTypeRepository.save(type);
    }

    @Test
    @Order(1)
    void shouldCreateRestaurantType() throws Exception {
        String json = """
            {
              "name": "Churrascaria"
            }
        """;

        var result = mockMvc.perform(post("/api/v1/restaurant-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        String id = result.getResponse().getContentAsString().replace("\"", "");
        createdId = UUID.fromString(id);

        assertThat(restaurantTypeRepository.findById(createdId)).isPresent();
    }

    @Test
    @Order(2)
    void shouldGetRestaurantTypeById() throws Exception {
        if (createdId == null) createdId = createMockRestaurantType().getId();

        mockMvc.perform(get("/api/v1/restaurant-types/{id}", createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdId.toString()));
    }

    @Test
    @Order(3)
    void shouldReturnNotFoundWhenRestaurantTypeIdNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/restaurant-types/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("ID não encontrado."));
    }

    @Test
    @Order(4)
    void shouldListAllRestaurantTypes() throws Exception {
        createMockRestaurantType();

        mockMvc.perform(get("/api/v1/restaurant-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").exists());
    }

    @Test
    @Order(5)
    void shouldUpdateRestaurantType() throws Exception {
        if (createdId == null) createdId = createMockRestaurantType().getId();

        String json = """
            {
              "name": "Japonesa"
            }
        """;

        mockMvc.perform(put("/api/v1/restaurant-types/{id}", createdId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        assertThat(restaurantTypeRepository.findById(createdId)).get().extracting(RestaurantTypeModel::getName).isEqualTo("Japonesa");
    }

    @Test
    @Order(6)
    void shouldDeleteRestaurantType() throws Exception {
        if (createdId == null) createdId = createMockRestaurantType().getId();

        mockMvc.perform(delete("/api/v1/restaurant-types/{id}", createdId))
                .andExpect(status().isNoContent());

        assertThat(restaurantTypeRepository.findById(createdId)).isNotPresent();
    }

    @Test
    @Order(7)
    void shouldReturnNoContentOnDeleteWhenIdNotFound() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        assertThat(restaurantTypeRepository.findById(nonExistentId)).isNotPresent();

        mockMvc.perform(delete("/api/v1/restaurant-types/{id}", nonExistentId))
                .andExpect(status().isNoContent());
    }

}
