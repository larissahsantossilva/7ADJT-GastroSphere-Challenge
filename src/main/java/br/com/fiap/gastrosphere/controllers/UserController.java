package br.com.fiap.gastrosphere.controllers;

import br.com.fiap.gastrosphere.dtos.LoginUserDTO;
import br.com.fiap.gastrosphere.dtos.requests.UserBodyRequest;
import br.com.fiap.gastrosphere.dtos.responses.UserBodyResponse;
import br.com.fiap.gastrosphere.entities.User;
import br.com.fiap.gastrosphere.exceptions.UnprocessableEntityException;
import br.com.fiap.gastrosphere.services.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.*;
import static br.com.fiap.gastrosphere.utils.GastroSphereUtils.convertToUser;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

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
                    @ApiResponse(
                            description = OK,
                            responseCode = HTTP_STATUS_CODE_200,
                            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserBodyResponse.class)))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<UserBodyResponse>> findAllUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        logger.info("GET | {} | Iniciado findAllUsers", V1_USER);
        Page<User> users = this.userService.findAllUsers(page, size);
        List<UserBodyResponse> userResponses = users.stream().map(UserBodyResponse::new).toList();
        logger.info("GET | {} | Finalizado findAllUsers", V1_USER);
        return ok(userResponses);
    }


    @Operation(
            description = "Busca usuário por id.",
            summary = "Busca usuário por id.",
            responses = {
                    @ApiResponse(
                            description = OK,
                            responseCode = HTTP_STATUS_CODE_200,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserBodyResponse.class))
                    ),
                    @ApiResponse(
                            description = NOT_FOUND,
                            responseCode = HTTP_STATUS_CODE_404,
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = Void.class))
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserBodyResponse> findUserById(@PathVariable("id") UUID id) {
        logger.info("GET | {} | Iniciado findUserById | id: {}", V1_USER, id);
        var user = userService.findById(id);
        if (user != null){
            logger.info("GET | {} | Finalizado findUserById | id: {}", V1_USER, id);
            return ok(new UserBodyResponse(user));
        }
        logger.info("GET | {} | Finalizado findUserById No Content | id: {}", V1_USER, id);
        return status(HttpStatus.NOT_FOUND).build();
    }


    @Operation(
            description = "Cria usuário.",
            summary = "Cria usuário.",
            responses = {
                    @ApiResponse(
                            description = USUARIO_CRIADO_COM_SUCESSO,
                            responseCode = HTTP_STATUS_CODE_201,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(
                            description = ERRO_AO_CRIAR_USUARIO,
                            responseCode = HTTP_STATUS_CODE_422,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    ),
            }
    )
    @PostMapping
    public ResponseEntity<UUID> createUser(@Valid @RequestBody UserBodyRequest userRequestBody) {
        logger.info("POST | {} | Iniciado createUser | User: {}", V1_USER, userRequestBody.getDocument());
        User user = userService.createUser(convertToUser(userRequestBody));
        logger.info("POST | {} | Finalizado createUser", V1_USER);
        return status(201).body(user.getId());
    }


    @Operation(
            description = "Atualiza usuário por id.",
            summary = "Atualiza usuário por id.",
            responses = {
                    @ApiResponse(
                            description = OK,
                            responseCode = HTTP_STATUS_CODE_200,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
                    ),
                    @ApiResponse(
                            description = USUARIO_NAO_ENCONTRADO,
                            responseCode = HTTP_STATUS_CODE_404,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(
                            description = ERRO_AO_ALTERAR_USUARIO,
                            responseCode = HTTP_STATUS_CODE_422,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable("id") UUID id, @Valid @RequestBody UserBodyRequest userRequestBody) {
        logger.info("PUT | {} | Iniciado updateUser | id: {}", V1_USER, id);
        userService.updateUser(convertToUser(userRequestBody), id);
        logger.info("PUT | {} | Finalizado updateUser", V1_USER);
        return ok("Usuário atualizado com sucesso");
    }


    @Operation(
            description = "Troca a senha de um usuário.",
            summary = "Troca a senha do usuário.",
            responses = {
                    @ApiResponse(
                            description = OK,
                            responseCode = HTTP_STATUS_CODE_200,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(
                            description = SENHA_ANTIGA_INCORRETA,
                            responseCode = HTTP_STATUS_CODE_400,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(
                            description = SENHA_NOVA_DEVE_SER_DIFERENTE,
                            responseCode = HTTP_STATUS_CODE_400,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(
                            description = USUARIO_NAO_ENCONTRADO,
                            responseCode = HTTP_STATUS_CODE_404,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(
                            description = ERRO_AO_ATUALIZAR_SENHA,
                            responseCode = HTTP_STATUS_CODE_422,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    )
            }
    )
    @PutMapping("/{id}/password")
    public ResponseEntity<String> updatePassword(@PathVariable("id") UUID id,
                                                 @Valid @RequestBody LoginUserDTO user) {
        logger.info("PUT | {} | Iniciado updatePassword | id: {}", V1_USER, id);
        userService.updatePassword(id, user.oldPassword(), user.newPassword());
        logger.info("PUT | {} | Finalizado updatePassword | id: {}", V1_USER, id);
        return ok("Senha atualizada com sucesso");
    }


    @Operation(
            description = "Exclui usuário por id.",
            summary = "Exclui usuário por id.",
            responses = {
                    @ApiResponse(
                            description = NO_CONTENT,
                            responseCode = HTTP_STATUS_CODE_204,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
                    ),
                    @ApiResponse(
                            description = USUARIO_NAO_ENCONTRADO,
                            responseCode = HTTP_STATUS_CODE_404,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") UUID id) {
        logger.info("DELETE | {} | Iniciado deleteUser | id: {}", V1_USER, id);
        try {
            userService.deleteUserById(id);
            logger.info("DELETE | {} | Usuário deletado com sucesso | Id: {}", V1_USER, id);
            return ResponseEntity.noContent().build();
        } catch (UnprocessableEntityException e) {
            logger.error("DELETE | {} | Erro ao deletar usuário | Id: {} | Erro: {}", V1_USER, id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
    }

}