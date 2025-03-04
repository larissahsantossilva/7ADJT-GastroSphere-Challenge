package br.com.fiap.gastrosphere.controllers;

import br.com.fiap.gastrosphere.dtos.requests.RestaurantBodyRequest;
import br.com.fiap.gastrosphere.entities.Address;
import br.com.fiap.gastrosphere.entities.Restaurant;
import br.com.fiap.gastrosphere.services.RestaurantServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.*;
import static br.com.fiap.gastrosphere.utils.GastroSphereUtils.convertToRestaurant;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(RestaurantController.V1_RESTAURANT)
@Tag(name = "RestaurantController", description = "Controller para CRUD de restaurantes.")
public class RestaurantController {

    public static final String V1_RESTAURANT = "/api/v1/restaurants";
    private static final Logger logger = getLogger(RestaurantController.class);
    private final RestaurantServiceImpl restaurantService;

    public RestaurantController(RestaurantServiceImpl restaurantService) {
        this.restaurantService = restaurantService;
    }

    @Operation(
            description = "Busca todos os restaurantes de forma paginada.",
            summary = "Busca todos os restaurantes de forma paginada.",
            responses = {
                    @ApiResponse(
                            description = OK,
                            responseCode = HTTP_STATUS_CODE_200,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Address.class))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<Restaurant>> findAllRestaurants(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        logger.info("GET | {} | Iniciado findAllRestaurants", V1_RESTAURANT);
        var restaurants = this.restaurantService.findAllRestaurants(page, size);
        logger.info("GET | {} | Finalizado findAllRestaurants", V1_RESTAURANT);
        return ok(restaurants);
    }

    @Operation(
            description = "Busca restaurante por id.",
            summary = "Busca restaurante por id.",
            responses = {
                    @ApiResponse(
                            description = OK,
                            responseCode = HTTP_STATUS_CODE_200,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Restaurant.class))
                    ),
                    @ApiResponse(
                            description = NO_CONTENT,
                            responseCode = HTTP_STATUS_CODE_204,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Restaurant>> findRestaurantById(@PathVariable("id") UUID id) {
        logger.info("GET | {} | Iniciado findRestaurantById | id: {}", V1_RESTAURANT, id);
        var restaurant = restaurantService.findById(id);
        if (restaurant.isPresent()) {
            logger.info("GET | {} | Finalizado findRestaurantById | id: {}", V1_RESTAURANT, id);
            return ok(restaurant);
        }
        logger.info("GET | {} | Finalizado √ No Content | id: {}", V1_RESTAURANT, id);
        return status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            description = "Cria restaurante.",
            summary = "Cria restaurante.",
            responses = {
                    @ApiResponse(
                            description = RESTAURANTE_CRIADO_COM_SUCESSO,
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
    public ResponseEntity<String> createRestaurant(@Valid @RequestBody RestaurantBodyRequest restaurantBodyRequest) {
        logger.info("POST | {} | Iniciado createRestaurant | Restaurant: {}", V1_RESTAURANT, restaurantBodyRequest.getName());
        restaurantService.createRestaurant(convertToRestaurant(restaurantBodyRequest));
        logger.info("POST | {} | Finalizado createRestaurant", V1_RESTAURANT);
        return status(201).body(RESTAURANTE_CRIADO_COM_SUCESSO);
    }

    @Operation(
            description = "Atualiza restaurante por id.",
            summary = "Atualiza restaurante por id.",
            responses = {
                    @ApiResponse(
                            description = OK,
                            responseCode = HTTP_STATUS_CODE_200,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
                    ),
                    @ApiResponse(
                            description = RESTAURANTE_NAO_ENCONTRADO,
                            responseCode = HTTP_STATUS_CODE_404,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(
                            description = ERRO_AO_ALTERAR_RESTAURANTE,
                            responseCode = HTTP_STATUS_CODE_422,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRestaurant(
            @PathVariable("id") UUID id,
            @Valid @RequestBody RestaurantBodyRequest restaurantBodyRequest) {
        logger.info("PUT | {} | Iniciado updateRestaurant | id: {}", V1_RESTAURANT, id);
        restaurantService.updateRestaurant(convertToRestaurant(restaurantBodyRequest), id);
        logger.info("PUT | {} | Finalizado updateRestaurant", V1_RESTAURANT);
        return status(HttpStatus.OK).build();
    }

    @Operation(
            description = "Exclui restaurante por id.",
            summary = "Exclui restaurante por id.",
            responses = {
                    @ApiResponse(
                            description = OK,
                            responseCode = HTTP_STATUS_CODE_200,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
                    ),
                    @ApiResponse(
                            description = RESTAURANTE_NAO_ENCONTRADO,
                            responseCode = HTTP_STATUS_CODE_404,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurantById(@PathVariable("id") UUID id) {
        logger.info("DELETE | {} | Iniciado deleteRestaurantById | id: {}", V1_RESTAURANT, id);
        restaurantService.deleteRestaurantById(id);
        logger.info("DELETE | {} | Finalizado deleteRestaurantById | id: {}", V1_RESTAURANT, id);
        return ok().build();
    }

}
