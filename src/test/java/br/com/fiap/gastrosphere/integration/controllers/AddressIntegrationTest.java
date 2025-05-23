package br.com.fiap.gastrosphere.integration.controllers;

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

import br.com.fiap.gastrosphere.core.infra.repository.AddressRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
class AddressIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AddressRepository addressRepository;

    private static UUID createdId;

    @Test
    @Order(1)
    void shouldCreateAddress() throws Exception {
        String json = """
                    {
                      "country": "Brasil",
                      "state": "São Paulo",
                      "city": "São Paulo",
                      "zipCode": "12345-678",
                      "street": "Rua Teste",
                      "number": "100",
                      "complement": "Ap 202"
                    }
                """;

        var result = mockMvc.perform(post("/api/v1/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNotEmpty())
                .andReturn();

        String id = result.getResponse().getContentAsString().replace("\"", "");
        createdId = UUID.fromString(id);
    }

    @Test
    @Order(2)
    void shouldGetAddressById() throws Exception {
        mockMvc.perform(get("/api/v1/addresses/{id}", createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdId.toString()));
    }

    @Test
    @Order(3)
    void shouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        UUID fakeId = UUID.randomUUID();
        mockMvc.perform(get("/api/v1/addresses/{id}", fakeId))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(4)
    void shouldGetAllAddressesWithoutZipCode() throws Exception {
        mockMvc.perform(get("/api/v1/addresses"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    void shouldGetAddressesByZipCode() throws Exception {
        mockMvc.perform(get("/api/v1/addresses")
                        .param("zipCode", "12345678"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    void shouldUpdateAddress() throws Exception {
        String json = """
        {
          "country": "Brasil",
          "state": "Rio de Janeiro",
          "city": "Rio de Janeiro",
          "zipCode": "12345-678",
          "street": "Av Nova",
          "number": "200",
          "complement": "Cobertura"
        }
    """;

        mockMvc.perform(put("/api/v1/addresses/{id}", createdId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    void shouldDeleteAddress() throws Exception {
        mockMvc.perform(delete("/api/v1/addresses/{id}", createdId))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(8)
    void shouldReturnNotFoundOnDeleteWhenIdNotFound() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        while (addressRepository.existsById(nonExistentId)) {
            nonExistentId = UUID.randomUUID();
        }

        mockMvc.perform(delete("/api/v1/addresses/{id}", nonExistentId))
                .andExpect(status().isNoContent());

    }

}
