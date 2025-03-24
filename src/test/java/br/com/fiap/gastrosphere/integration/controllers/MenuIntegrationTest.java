package br.com.fiap.gastrosphere.integration.controllers;

import static org.assertj.core.api.Assertions.assertThat;
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

import br.com.fiap.gastrosphere.core.infra.repository.MenuRepository;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
class MenuIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private MenuRepository menuRepository;

	private static UUID createdMenuId;

	@Test
	@Order(1)
	void shouldCreateMenu() throws Exception {
	    String json = """
	    {
	      "name": "Menu de Teste",
	      "restaurantId": "00000000-0000-0000-0000-000000000003"
	    }
	    """;

	    var result = mockMvc.perform(post("/api/v1/menus")
	                    .contentType(MediaType.APPLICATION_JSON)
	                    .content(json))
	            .andExpect(status().isCreated())
	            .andExpect(jsonPath("$").isNotEmpty())
	            .andReturn();

	    String id = result.getResponse().getContentAsString().replace("\"", "");
	    createdMenuId = UUID.fromString(id);
	    assertThat(createdMenuId).isNotNull();
	}

	@Test
	@Order(2)
	void shouldGetMenuById() throws Exception {
	    assertThat(menuRepository.findById(createdMenuId)).isPresent();

	    mockMvc.perform(get("/api/v1/menus/{id}", createdMenuId))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.id").value(createdMenuId.toString()));
	}

	@Test
	@Order(3)
	void shouldReturnNotFoundWhenMenuIdDoesNotExist() throws Exception {
		UUID fakeId = UUID.randomUUID();
		mockMvc.perform(get("/api/v1/menus/{id}", fakeId)).andExpect(status().isNotFound());
	}

	@Test
	@Order(4)
	void shouldListAllMenus() throws Exception {
		mockMvc.perform(get("/api/v1/menus")).andExpect(status().isOk());
	}

	@Test
	@Order(5)
	void shouldUpdateMenu() throws Exception {
		String json = """
				{
				  "name": "Menu Atualizado",
				  "restaurantId": "00000000-0000-0000-0000-000000000003"
				}
				""";
		assertThat(menuRepository.findById(createdMenuId)).isPresent();
		mockMvc.perform(put("/api/v1/menus/{id}", createdMenuId).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk());
	}

	@Test
	@Order(6)
	void shouldDeleteMenu() throws Exception {
		mockMvc.perform(delete("/api/v1/menus/{id}", createdMenuId)).andExpect(status().isNoContent());
	}

	@Test
	@Order(7)
	void shouldReturnNotFoundOnDeleteWhenMenuIdDoesNotExist() throws Exception {
		String nonExistentId = "Teste";

		mockMvc.perform(delete("/api/v1/menus/{id}", nonExistentId)).andExpect(status().isNotFound());
	}
}
