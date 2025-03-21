package br.com.fiap.gastrosphere.core.application.controller;

import br.com.fiap.gastrosphere.core.domain.entity.RestaurantTypeBodyRequest;
import br.com.fiap.gastrosphere.core.infra.model.RestaurantTypeModel;
import br.com.fiap.gastrosphere.core.domain.entity.RestaurantType;
import br.com.fiap.gastrosphere.exceptions.UnprocessableEntityException;
import br.com.fiap.gastrosphere.core.application.service.RestaurantTypeServiceImpl;
import br.com.fiap.gastrosphere.core.application.utils.GastroSphereUtils;
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

import java.util.List;
import java.util.UUID;

import static br.com.fiap.gastrosphere.core.application.utils.GastroSphereConstants.*;
import static br.com.fiap.gastrosphere.core.application.utils.GastroSphereUtils.convertToRestaurantType;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(RestaurantTypeController.V1_RESTAURANT_TYPE)
@Tag(name = "RestaurantTypeController", description = "Controller para CRUD de tipos de restaurante.")
public class RestaurantTypeController {

    public static final String V1_RESTAURANT_TYPE = "/api/v1/restaurant-types";
    private static final Logger logger = getLogger(RestaurantTypeController.class);
    private final RestaurantTypeServiceImpl restaurantTypeService;

    public RestaurantTypeController(RestaurantTypeServiceImpl restaurantTypeService) {
        this.restaurantTypeService = restaurantTypeService;
    }

    @Operation(
            description = "Busca todos os tipos de restaurante de forma paginada.",
            summary = "Busca todos os tipos de restaurante de forma paginada.",
            responses = {
                    @ApiResponse(
                            description = OK,
                            responseCode = HTTP_STATUS_CODE_200,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestaurantType.class))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<RestaurantType>> findAllRestaurantTypes(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        logger.info("GET | {} | Iniciado findAllRestaurantTypes", V1_RESTAURANT_TYPE);
        Page<RestaurantTypeModel> restaurantTypesModel = this.restaurantTypeService.findAllRestaurantTypes(page, size);
        List<RestaurantType> restaurantTypes = restaurantTypesModel.stream()
                .map(GastroSphereUtils::convertToRestaurantType)
                .toList();
        logger.info("GET | {} | Finalizado findAllRestaurantTypes", V1_RESTAURANT_TYPE);
        return ok(restaurantTypes);
    }

    @Operation(
            description = "Busca tipo de restaurante por id.",
            summary = "Busca tipo de restaurante por id.",
            responses = {
                    @ApiResponse(
                            description = OK,
                            responseCode = HTTP_STATUS_CODE_200,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestaurantType.class))
                    ),
                    @ApiResponse(
                            description = NOT_FOUND,
                            responseCode = HTTP_STATUS_CODE_404,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantType> findRestaurantTypeById(@PathVariable("id") UUID id) {
        logger.info("GET | {} | Iniciado findRestaurantTypeById | id: {}", V1_RESTAURANT_TYPE, id);
        var restaurantTypeModel = restaurantTypeService.findById(id);
        RestaurantType restaurantType = convertToRestaurantType(restaurantTypeModel);
        return restaurantType != null ? ok(restaurantType) : status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(
            description = "Cria tipo de restaurante.",
            summary = "Cria tipo de restaurante.",
            responses = {
                    @ApiResponse(
                            description = TIPO_RESTAURANTE_CRIADO_COM_SUCESSO,
                            responseCode = HTTP_STATUS_CODE_201,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(
                            description = ERRO_AO_CRIAR_TIPO_RESTAURANTE,
                            responseCode = HTTP_STATUS_CODE_422,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    ),
            }
    )
    @PostMapping
        public ResponseEntity<UUID> createRestaurantType(@Valid @RequestBody RestaurantTypeBodyRequest restaurantTypeBodyRequest) {
        logger.info("POST | {} | Iniciado createRestaurantType | Restaurant: {}", V1_RESTAURANT_TYPE, restaurantTypeBodyRequest.getName());
        RestaurantTypeModel restaurantType = restaurantTypeService.createRestaurantType(convertToRestaurantType(restaurantTypeBodyRequest));
        logger.info("POST | {} | Finalizado createRestaurantType", V1_RESTAURANT_TYPE);
        return status(201).body(restaurantType.getId());
    }

    @Operation(
            description = "Atualiza tipo de restaurante por id.",
            summary = "Atualiza tipo de restaurante por id.",
            responses = {
                    @ApiResponse(
                            description = OK,
                            responseCode = HTTP_STATUS_CODE_200,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
                    ),
                    @ApiResponse(
                            description = TIPO_RESTAURANTE_NAO_ENCONTRADO,
                            responseCode = HTTP_STATUS_CODE_404,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(
                            description = ERRO_AO_ALTERAR_TIPO_RESTAURANTE,
                            responseCode = HTTP_STATUS_CODE_422,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<String> updateRestaurantType(@PathVariable("id") UUID id, @Valid @RequestBody RestaurantTypeBodyRequest restaurantTypeBodyRequest) {
        logger.info("PUT | {} | Iniciado updateRestaurantType | id: {}", V1_RESTAURANT_TYPE, id);
        restaurantTypeService.updateRestaurantType(convertToRestaurantType(restaurantTypeBodyRequest), id);
        logger.info("PUT | {} | Finalizado updateRestaurantType", V1_RESTAURANT_TYPE);
        return ok("Tipo de restaurante atualizado com sucesso");
    }

    @Operation(
            description = "Exclui tipo de restaurante por id.",
            summary = "Exclui tipo de restaurante por id.",
            responses = {
                    @ApiResponse(
                            description = NO_CONTENT,
                            responseCode = HTTP_STATUS_CODE_204,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
                    ),
                    @ApiResponse(
                            description = TIPO_RESTAURANTE_NAO_ENCONTRADO,
                            responseCode = HTTP_STATUS_CODE_404,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRestaurantType(@PathVariable("id") UUID id) {
        logger.info("DELETE | {} | Iniciado deleteRestaurantType | id: {}", V1_RESTAURANT_TYPE, id);
        try {
            restaurantTypeService.deleteRestaurantTypeById(id);
            logger.info("DELETE | {} | Tipo de restaurante deletado com sucesso | Id: {}", V1_RESTAURANT_TYPE, id);
            return ResponseEntity.noContent().build();
        } catch (UnprocessableEntityException e) {
            logger.error("DELETE | {} | Erro ao deletar tipo de restaurante | Id: {} | Erro: {}", V1_RESTAURANT_TYPE, id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tipo de restaurante não encontrado");
        }
    }

}
