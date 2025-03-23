/*
package br.com.fiap.gastrosphere.integration;

import br.com.fiap.gastrosphere.dtos.LoginUserDTO;
import br.com.fiap.gastrosphere.dtos.requests.AddressBodyRequest;
import br.com.fiap.gastrosphere.dtos.requests.UserBodyRequest;
import br.com.fiap.gastrosphere.dtos.requests.UserTypeBodyRequest;
import br.com.fiap.gastrosphere.entities.Address;
import br.com.fiap.gastrosphere.entities.User;
import br.com.fiap.gastrosphere.entities.UserType;
import br.com.fiap.gastrosphere.repositories.UserRepository;
import br.com.fiap.gastrosphere.repositories.UserTypeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
public class UserIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserTypeRepository userTypeRepository;

    private UserType userType;
    private User user;
    private Address address;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        userTypeRepository.deleteAll();

        userType = new UserType(null, "ADMIN", null, null);
        userType = userTypeRepository.save(userType);

        address = new Address();
        address.setStreet("Rua Teste");
        address.setNumber("123");

        user = new User();
        user.setName("João da Silva");
        user.setEmail("joao@email.com");
        user.setLogin("joaosilva");
        user.setPassword("senha123");
        user.setDocument("12345678901");
        user.setAddress(address);
        user.setUserType(userType);

        user = userRepository.save(user);
    }

    private UserTypeBodyRequest mapToUserTypeDTO(UserType userType) {
        return new UserTypeBodyRequest(userType.getName());
    }

    private AddressBodyRequest mapToAddressDTO(Address address) {
        AddressBodyRequest dto = new AddressBodyRequest();
        dto.setStreet(address.getStreet());
        dto.setNumber(address.getNumber());
        return dto;
    }

    @Test
    @Order(1)
    void shouldFindUserById() throws Exception {
        mockMvc.perform(get("/api/v1/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("João da Silva"));
    }

    @Test
    @Order(2)
    void shouldReturnNotFound_whenUserDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/users/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(3)
    void shouldCreateUser() throws Exception {
        UserBodyRequest request = new UserBodyRequest();
        request.setName("Maria");
        request.setEmail("maria@email.com");
        request.setLogin("maria123");
        request.setPassword("senha456");
        request.setDocument("99988877766");
        request.setUserType(mapToUserTypeDTO(userType));
        request.setAddress(mapToAddressDTO(address));

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    @Order(4)
    void shouldUpdateUser() throws Exception {
        UserBodyRequest request = new UserBodyRequest();
        request.setName("Novo Nome");
        request.setEmail(user.getEmail());
        request.setLogin(user.getLogin());
        request.setPassword(user.getPassword());
        request.setDocument(user.getDocument());
        request.setUserType(mapToUserTypeDTO(user.getUserType()));
        request.setAddress(mapToAddressDTO(user.getAddress()));

        mockMvc.perform(put("/api/v1/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuário atualizado com sucesso"));

        var updated = userRepository.findById(user.getId()).orElseThrow();
        assertThat(updated.getName()).isEqualTo("Novo Nome");
    }

    @Test
    @Order(5)
    void shouldChangeUserPassword() throws Exception {
        LoginUserDTO dto = new LoginUserDTO();
        dto.setOldPassword("senha123");
        dto.setNewPassword("novaSenha");

        mockMvc.perform(put("/api/v1/users/" + user.getId() + "/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Senha atualizada com sucesso"));
    }

    @Test
    @Order(6)
    void shouldReturnBadRequest_whenOldPasswordIsWrong() throws Exception {
        LoginUserDTO dto = new LoginUserDTO();
        dto.setOldPassword("senhaErrada");
        dto.setNewPassword("novaSenha");

        mockMvc.perform(put("/api/v1/users/" + user.getId() + "/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Senha antiga incorreta"));
    }

    @Test
    @Order(7)
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/v1/users/" + user.getId()))
                .andExpect(status().isNoContent());

        assertThat(userRepository.findById(user.getId())).isEmpty();
    }

    @Test
    @Order(8)
    void shouldReturnNotFound_whenDeletingNonexistentUser() throws Exception {
        mockMvc.perform(delete("/api/v1/users/" + UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Usuário não encontrado"));
    }
}
*/
