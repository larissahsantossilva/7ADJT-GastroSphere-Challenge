package br.com.fiap.gastrosphere.integration.controllers;

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

import br.com.fiap.gastrosphere.core.infra.repository.UserTypeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserTypeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserTypeRepository userTypeRepository;

    private static UUID createdUserTypeId;

    @Test
    @Order(1)
    void shouldCreateUserType() throws Exception {
        String json = """
        {
          "name": "Administrador"
        }
        """;

        var result = mockMvc.perform(post("/api/v1/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNotEmpty())
                .andReturn();

        String id = result.getResponse().getContentAsString().replace("\"", "");
        createdUserTypeId = UUID.fromString(id);
    }

    @Test
    @Order(2)
    void shouldGetUserTypeById() throws Exception {
        mockMvc.perform(get("/api/v1/user-types/{id}", createdUserTypeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdUserTypeId.toString()));
    }

    @Test
    @Order(3)
    void shouldReturnNotFoundWhenUserTypeDoesNotExist() throws Exception {
        UUID fakeId = UUID.randomUUID();
        mockMvc.perform(get("/api/v1/user-types/{id}", fakeId))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(4)
    void shouldListAllUserTypes() throws Exception {
        mockMvc.perform(get("/api/v1/user-types")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @Order(5)
    void shouldUpdateUserType() throws Exception {
        String json = """
        {
          "name": "Administrador Atualizado"
        }
        """;

        mockMvc.perform(put("/api/v1/user-types/{id}", createdUserTypeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    void shouldDeleteUserType() throws Exception {
        mockMvc.perform(delete("/api/v1/user-types/{id}", createdUserTypeId))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(7)
    void shouldReturnNotFoundOnDeleteWhenUserTypeDoesNotExist() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        while (userTypeRepository.existsById(nonExistentId)) {
            nonExistentId = UUID.randomUUID();
        }

        mockMvc.perform(delete("/api/v1/user-types/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }
}
