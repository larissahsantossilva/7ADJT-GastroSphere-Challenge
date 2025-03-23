package br.com.fiap.gastrosphere.controllers;

import br.com.fiap.gastrosphere.dtos.LoginUserDTO;
import br.com.fiap.gastrosphere.dtos.requests.UserBodyRequest;
import br.com.fiap.gastrosphere.dtos.responses.UserBodyResponse;
import br.com.fiap.gastrosphere.entities.User;
import br.com.fiap.gastrosphere.exceptions.UnprocessableEntityException;
import br.com.fiap.gastrosphere.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserServiceImpl userService;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userService = mock(UserServiceImpl.class);
        userController = new UserController(userService);
    }

    @Test
    void findAllUsers_shouldReturnListOfUsers() {
        User user = new User();
        when(userService.findAllUsers(0, 10))
                .thenReturn(new PageImpl<>(List.of(user), PageRequest.of(0, 10), 1));

        ResponseEntity<List<UserBodyResponse>> response = userController.findAllUsers(0, 10);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(userService).findAllUsers(0, 10);
    }

    @Test
    void findUserById_shouldReturnUserWhenExists() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setId(id);

        when(userService.findById(id)).thenReturn(user);

        ResponseEntity<UserBodyResponse> response = userController.findUserById(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        verify(userService).findById(id);
    }

    @Test
    void findUserById_shouldReturnNotFoundWhenNotExists() {
        UUID id = UUID.randomUUID();

        when(userService.findById(id)).thenReturn(null);

        ResponseEntity<UserBodyResponse> response = userController.findUserById(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void createUser_shouldReturnCreatedUserId() {
        UUID id = UUID.randomUUID();
        UserBodyRequest request = new UserBodyRequest();
        User user = new User();
        user.setId(id);

        when(userService.createUser(any(User.class))).thenReturn(user);

        ResponseEntity<UUID> response = userController.createUser(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(id);
    }

    @Test
    void updateUser_shouldReturnSuccessMessage() {
        UUID id = UUID.randomUUID();
        UserBodyRequest request = new UserBodyRequest();
        User user = new User();
        user.setId(id);

        when(userService.updateUser(any(User.class), eq(id))).thenReturn(user);

        ResponseEntity<String> response = userController.updateUser(id, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Usuário atualizado com sucesso");
    }

    @Test
    void updatePassword_shouldReturnSuccessMessage() {
        UUID id = UUID.randomUUID();
        LoginUserDTO dto = new LoginUserDTO();
        dto.setOldPassword("old");
        dto.setNewPassword("new");

        doNothing().when(userService).updatePassword(id, "old", "new");

        ResponseEntity<String> response = userController.updatePassword(id, dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Senha atualizada com sucesso");
    }

    @Test
    void deleteUser_shouldReturnNoContent() {
        UUID id = UUID.randomUUID();

        doNothing().when(userService).deleteUserById(id);

        ResponseEntity<String> response = userController.deleteUser(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void deleteUser_shouldReturnNotFoundWhenExceptionThrown() {
        UUID id = UUID.randomUUID();

        doThrow(new UnprocessableEntityException("Usuário não encontrado"))
                .when(userService).deleteUserById(id);

        ResponseEntity<String> response = userController.deleteUser(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Usuário não encontrado");
    }


}
