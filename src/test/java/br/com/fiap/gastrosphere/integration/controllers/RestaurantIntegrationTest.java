package br.com.fiap.gastrosphere.integration.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fiap.gastrosphere.core.infra.model.AddressModel;
import br.com.fiap.gastrosphere.core.infra.model.RestaurantModel;
import br.com.fiap.gastrosphere.core.infra.model.RestaurantTypeModel;
import br.com.fiap.gastrosphere.core.infra.model.UserModel;
import br.com.fiap.gastrosphere.core.infra.repository.RestaurantRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RestaurantIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private static UUID createdId;

    private RestaurantModel createMockRestaurant() {
        AddressModel address = new AddressModel();
        address.setCountry("Brasil");
        address.setZipCode("00000-000");
        address.setStreet("Rua Teste");
        address.setNumber("123");
        address.setCreatedAt(LocalDate.now());
        address.setLastModifiedAt(LocalDate.now());

        RestaurantTypeModel type = new RestaurantTypeModel();
        type.setName("Italiana");
        type.setCreatedAt(LocalDate.now());
        type.setLastModifiedAt(LocalDate.now());

        UserModel user = new UserModel();
        user.setName("João");
        user.setEmail("joao@example.com");
        user.setLogin("joao123");
        user.setPassword("senha123");
        user.setDocument("12345678900");
        user.setUserType(null);
        user.setAddress(address);
        user.setCreatedAt(LocalDate.now());
        user.setLastModifiedAt(LocalDate.now());

        RestaurantModel restaurant = new RestaurantModel();
        restaurant.setName("Spoleto");
        restaurant.setAddress(address);
        restaurant.setRestaurantType(type);
        restaurant.setUser(user);
        restaurant.setStartedAt(LocalTime.of(9, 0));
        restaurant.setFinishedAt(LocalTime.of(22, 0));
        restaurant.setCreatedAt(LocalDate.now());
        restaurant.setLastModifiedAt(LocalDate.now());

        return restaurantRepository.save(restaurant);
    }


    @Test
    @Order(1)
    void shouldCreateRestaurant() throws Exception {
        RestaurantModel restaurant = createMockRestaurant();
        String json = objectMapper.writeValueAsString(restaurant);

        var result = mockMvc.perform(post("/api/v1/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        createdId = UUID.fromString(result.getResponse().getContentAsString().replace("\"", ""));
        assertThat(restaurantRepository.findById(createdId)).isPresent();
    }

    @Test
    @Order(2)
    void shouldGetRestaurantById() throws Exception {
        if (createdId == null) createdId = createMockRestaurant().getId();

        mockMvc.perform(get("/api/v1/restaurants/{id}", createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdId.toString()));
    }

    @Test
    @Order(3)
    void shouldReturnNotFoundWhenRestaurantIdNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/restaurants/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("ID não encontrado."));
    }

    @Test
    @Order(4)
    void shouldListAllRestaurants() throws Exception {
        createMockRestaurant();

        mockMvc.perform(get("/api/v1/restaurants"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    void shouldUpdateRestaurant() throws Exception {
        if (createdId == null) createdId = createMockRestaurant().getId();

        String json = """
            {
              "name": "Outback"
            }
        """;

        mockMvc.perform(put("/api/v1/restaurants/{id}", createdId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json));

        assertThat(restaurantRepository.findById(createdId)).get().extracting(RestaurantModel::getName).isEqualTo("Spoleto");
    }

    @Test
    @Order(6)
    void shouldDeleteRestaurant() throws Exception {
        if (createdId == null) createdId = createMockRestaurant().getId();

        mockMvc.perform(delete("/api/v1/restaurants/{id}", createdId))
                .andExpect(status().isNoContent());

        assertThat(restaurantRepository.findById(createdId)).isNotPresent();
    }

}
