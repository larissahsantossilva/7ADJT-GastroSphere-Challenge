package br.com.fiap.gastrosphere.controllers;

import br.com.fiap.gastrosphere.dtos.requests.UserTypeBodyRequest;
import br.com.fiap.gastrosphere.entities.Address;
import br.com.fiap.gastrosphere.entities.UserType;
import br.com.fiap.gastrosphere.exceptions.ResourceNotFoundException;
import br.com.fiap.gastrosphere.exceptions.UnprocessableEntityException;
import br.com.fiap.gastrosphere.services.UserTypeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
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

import java.util.UUID;

import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.*;
import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.HTTP_STATUS_CODE_422;
import static br.com.fiap.gastrosphere.utils.GastroSphereUtils.convertToUserType;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(UserTypeController.V1_USERTYPE)
@Tag(name = "UserTypeController", description = "Controller para CRUD de tipos de usuário.")
public class UserTypeController {

    static final String V1_USERTYPE = "/api/v1/user-types";
    private static final Logger logger = getLogger(UserTypeController.class);
    private final UserTypeServiceImpl userTypeService;

    public UserTypeController(UserTypeServiceImpl userTypeService) {
        this.userTypeService = userTypeService;
    }

    @Operation(
            description = "Busca todos os tipos de usuário de forma paginada.",
            summary = "Busca todos os tipos de usuário.",
            responses = {
                    @ApiResponse(
                            description = OK,
                            responseCode = HTTP_STATUS_CODE_200,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Address.class))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<Page<UserType>> findAllUserTypes(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        logger.info("GET | {} | Iniciado busca de tipos de usuário", V1_USERTYPE);
        Page<UserType> userTypes = userTypeService.findAllUserTypes(page, size);
        logger.info("GET | {} | Finalizado busca de tipos de usuário", V1_USERTYPE);
        return ok(userTypes);
    }

    @Operation(
            description = "Busca tipo de usuário por id.",
            summary = "Busca tipo de usuário por id.",
            responses = {
                    @ApiResponse(
                            description = OK,
                            responseCode = HTTP_STATUS_CODE_200,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Address.class))
                    ),
                    @ApiResponse(
                            description = NOT_FOUND,
                            responseCode = HTTP_STATUS_CODE_404,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserType> findUserTypeById(@PathVariable("id") UUID id) {
        logger.info("GET | {} | Iniciado findUserTypeById | id: {}", V1_USERTYPE, id);
        var userType = userTypeService.findById(id);
        return userType != null ? ok(userType) : status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(
            description = "Cria tipo de usuário.",
            summary = "Cria tipo de usuário.",
            responses = {
                    @ApiResponse(
                            description = TIPO_USUARIO_CRIADO_COM_SUCESSO,
                            responseCode = HTTP_STATUS_CODE_201,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(
                            description = ERRO_AO_CRIAR_TIPO_USUARIO,
                            responseCode = HTTP_STATUS_CODE_422,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    )
            }
    )
    @PostMapping
    public ResponseEntity<UUID> createUserType(@Valid @RequestBody UserTypeBodyRequest userTypeDto) {
        logger.info("POST | {} | Iniciado createUserType.", V1_USERTYPE);
        UserType createdUserType = userTypeService.createUserType(convertToUserType(userTypeDto));
        logger.info("POST | {} | Finalizado createUserType", V1_USERTYPE);
        return status(HttpStatus.CREATED).body(createdUserType.getId());
    }

    @Operation(
            description = "Atualiza tipo de usuário por id.",
            summary = "Atualiza tipo de usuário.",
            responses = {
                    @ApiResponse(
                            description = OK,
                            responseCode = HTTP_STATUS_CODE_200,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
                    ),
                    @ApiResponse(
                            description = TIPO_USUARIO_NAO_ENCONTRADO,
                            responseCode = HTTP_STATUS_CODE_404,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(
                            description = ERRO_AO_ALTERAR_TIPO_USUARIO,
                            responseCode = HTTP_STATUS_CODE_422,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUserType(@PathVariable("id") UUID id, @Valid @RequestBody UserTypeBodyRequest userTypeDto) {
        logger.info("PUT | {} | Iniciado updateUserType | Id: {} | Dados: {}", V1_USERTYPE, id, userTypeDto);
        userTypeService.updateUserType(id, convertToUserType(userTypeDto));
        logger.info("PUT | {} | Finalizado updateUserType | Id: {}", V1_USERTYPE, id);
        return ok("Tipo de usuário atualizado com sucesso");
    }

    @Operation(
            description = "Exclui tipo de usuário por id.",
            summary = "Exclui tipo de usuário.",
            responses = {
                    @ApiResponse(
                            description = NO_CONTENT,
                            responseCode = HTTP_STATUS_CODE_204,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
                    ),
                    @ApiResponse(
                            description = TIPO_USUARIO_NAO_ENCONTRADO,
                            responseCode = HTTP_STATUS_CODE_404,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserType(@PathVariable("id") UUID id) {
        logger.info("DELETE | {} | Iniciado deleteUserType | Id: {}", V1_USERTYPE, id);
        try {
            userTypeService.deleteUserTypeById(id);
            logger.info("DELETE | {} | Tipo de usuário deletado com sucesso | Id: {}", V1_USERTYPE, id);
            return ResponseEntity.noContent().build();
        } catch (UnprocessableEntityException e) {
            logger.error("DELETE | {} | Erro ao deletar tipo de usuário | Id: {} | Erro: {}", V1_USERTYPE, id, e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            logger.error("DELETE | {} | Tipo de usuário não encontrado | Id: {} | Erro: {}", V1_USERTYPE, id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }
}
