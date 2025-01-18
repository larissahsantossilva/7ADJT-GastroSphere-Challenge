package br.com.fiap.gastrosphere.controllers;

import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.*;
import static br.com.fiap.gastrosphere.utils.GastroSphereUtils.convertToUser;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.fiap.gastrosphere.dtos.responses.UserBodyResponse;
import br.com.fiap.gastrosphere.dtos.requests.UserBodyRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.fiap.gastrosphere.dtos.LoginUserDto;
import br.com.fiap.gastrosphere.services.UserServiceImpl;

@RestController
@RequestMapping(UserController.V1_USER)
@Tag(name = "UserController", description = "Controller para CRUD de usuários.")
public class UserController {

    public static final String V1_USER = "/api/v1/users";
    private static final Logger logger = getLogger(UserController.class);
    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Operation(
        description = "Busca todos os usuários de forma paginada.",
        summary = "Busca todos os usuários de forma paginada.",
        responses = {
            @ApiResponse(description = OK, responseCode = HTTP_STATUS_CODE_200)
        }
    )
    @GetMapping
    public ResponseEntity<List<UserBodyResponse>> findAllUsers(@RequestParam(value = "page", defaultValue = "1") int page,
                                                               @RequestParam(value = "size", defaultValue = "10") int size) {
        logger.info("GET | {} | Iniciado findAllUsers", V1_USER);
        var users = this.userService.findAllUsers(page, size);
        logger.info("GET | {} | Finalizado findAllUsers", V1_USER);
        return ok(users);
    }

    @Operation(
        description = "Busca usuários por id.",
        summary = "Busca usuários por id.",
        responses = {
            @ApiResponse(description = OK, responseCode = HTTP_STATUS_CODE_200),
            @ApiResponse(description = NO_CONTENT, responseCode = HTTP_STATUS_CODE_204)
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Optional<UserBodyResponse>> findUserById(@PathVariable("id") UUID id) {
        logger.info("GET | {} | Iniciado findUserById | id: {}", V1_USER, id);
        var user = userService.findById(id);
        if(user.isPresent()){
            logger.info("GET | {} | Finalizado findUserById | id: {}", V1_USER, id);
            return ok(user);
        }
        logger.info("GET | {} | Finalizado findUserById No Content | id: {}", V1_USER, id);
        return status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
        description = "Cria usuário.",
        summary = "Cria usuário.",
        responses = {
            @ApiResponse(description = USUARIO_CRIADO_COM_SUCESSO, responseCode = HTTP_STATUS_CODE_201),
            @ApiResponse(description = ERRO_AO_CRIAR_USUARIO, responseCode = HTTP_STATUS_CODE_422),
        }
    )
    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody UserBodyRequest userRequestBody) {
        logger.info("POST | {} | Iniciado createUser | User: {}", V1_USER, userRequestBody.getDocument());
        userService.createUser(convertToUser(userRequestBody));
        logger.info("POST | {} | Finalizado createUser", V1_USER);
        return status(201).body(USUARIO_CRIADO_COM_SUCESSO);
    }

    @Operation(
        description = "Atualiza usuário por id.",
        summary = "Atualiza usuário por id.",
        responses = {
            @ApiResponse(description = OK, responseCode = HTTP_STATUS_CODE_200),
            @ApiResponse(description = USUARIO_NAO_ENCONTRADO, responseCode = HTTP_STATUS_CODE_404),
            @ApiResponse(description = ERRO_AO_ALTERAR_USUARIO, responseCode = HTTP_STATUS_CODE_422)
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable("id") UUID id,
                                           @Valid @RequestBody UserBodyRequest userRequestBody) {
        logger.info("PUT | {} | Iniciado updateUser | id: {}", V1_USER, id);
        userService.updateUser(convertToUser(userRequestBody), id);
        logger.info("PUT | {} | Finalizado updateUser", V1_USER);
        return status(HttpStatus.OK).build();
    }

    @Operation(
        description = "Troca a senha de um usuário.",
        summary = "Troca a senha do usuário.",
        responses = {
            @ApiResponse(description = OK, responseCode = HTTP_STATUS_CODE_200),
            @ApiResponse(description = SENHA_ANTIGA_INCORRETA, responseCode = HTTP_STATUS_CODE_400),
            @ApiResponse(description = SENHA_NOVA_DEVE_SER_DIFERENTE, responseCode = HTTP_STATUS_CODE_400),
            @ApiResponse(description = USUARIO_NAO_ENCONTRADO, responseCode = HTTP_STATUS_CODE_404),
            @ApiResponse(description = ERRO_AO_ATUALIZAR_SENHA, responseCode = HTTP_STATUS_CODE_422)
        }
    )
    @PutMapping("/{id}/password")
    public ResponseEntity<String> updatePassword(@PathVariable("id") UUID id,
                                                 @Valid @RequestBody LoginUserDto user) {
        logger.info("PUT | {} | Iniciado updatePassword | id: {}", V1_USER, id);
        userService.updatePassword(id, user.oldPassword(), user.newPassword());
        logger.info("PUT | {} | Finalizado updatePassword | id: {}", V1_USER, id);
        return ok("Senha atualizada com sucesso.");
    }

    @Operation(
        description = "Exclui usuário por id.",
        summary = "Exclui usuário por id.",
        responses = {
            @ApiResponse(description = OK, responseCode = HTTP_STATUS_CODE_200),
            @ApiResponse(description = USUARIO_NAO_ENCONTRADO, responseCode = HTTP_STATUS_CODE_404)
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") UUID id) {
        logger.info("DELETE | {} | Iniciado deleteUserById | id: {}", V1_USER, id);
        userService.deleteUserById(id);
        logger.info("DELETE | {} | Finalizado deleteUserByUd | id: {}", V1_USER, id);
        return ok().build();
    }
}
