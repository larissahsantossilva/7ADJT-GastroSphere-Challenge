package br.com.fiap.gastrosphere.integration.controllers;

import static org.springframework.http.MediaType.APPLICATION_JSON;
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

@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static UUID createdUserId;

    @Test
    @Order(1)
    void shouldCreateUser() throws Exception {
        String json = """
    			{
    			  "name": "João Teste",
    			  "email": "joao.teste@example.com",
    			  "login": "joaoteste",
    			  "password": "senha123",
    			  "document": "12345678900",
    			  "address": {
				  "country": "Brasil",
				  "state": "SP",
				  "city": "São Paulo",
				  "zipCode": "01001-000",
				  "street": "Av. Paulista",
				  "number": "1000"
				  },
    			  "userType": {
    			  	"name": "Administrador"
    			  }
    			}
    			""";
        var result = mockMvc.perform(post("/api/v1/users")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNotEmpty())
                .andReturn();
        String id = result.getResponse().getContentAsString().replace("\"", "");
        createdUserId = UUID.fromString(id);
    }

    @Test
    @Order(2)
    void shouldGetUserById() throws Exception {
        mockMvc.perform(get("/api/v1/users/{id}", createdUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdUserId.toString()));
    }

    @Test
    @Order(3)
    void shouldReturnNotFoundWhenUserIdDoesNotExist() throws Exception {
        UUID fakeId = UUID.randomUUID();
        mockMvc.perform(get("/api/v1/users/{id}", fakeId))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(4)
    void shouldGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/v1/users")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(5)
    void shouldUpdateUser() throws Exception {
        String json = """
        		{
    			  "name": "João Teste Atualizado",
    			  "email": "joao.teste@example.com",
    			  "login": "joaoteste",
    			  "password": "senha123",
    			  "document": "12345678900",
    			  "address": {
				  "country": "Brasil",
				  "state": "SP",
				  "city": "São Paulo",
				  "zipCode": "01001-000",
				  "street": "Av. Paulista",
				  "number": "1000"
				  },
    			  "userType": {
    			  	"name": "Administrador"
    			  }
    			}
        """;
        mockMvc.perform(put("/api/v1/users/{id}", createdUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    void shouldUpdatePassword() throws Exception {
        String json = """
        {
          "oldPassword": "senha123",
          "newPassword": "novaSenha123"
        }
        """;

        mockMvc.perform(put("/api/v1/users/{id}/password", createdUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/v1/users/{id}", createdUserId))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(8)
    void shouldReturnNotFoundOnDeleteWhenUserIdDoesNotExist() throws Exception {
        String nonExistentId = "1223";
        mockMvc.perform(delete("/api/v1/users/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }
}
