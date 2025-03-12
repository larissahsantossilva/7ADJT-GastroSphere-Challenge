package br.com.fiap.gastrosphere.controllers;

import br.com.fiap.gastrosphere.dtos.requests.RestaurantBodyRequest;
import br.com.fiap.gastrosphere.dtos.responses.RestaurantBodyResponse;
import br.com.fiap.gastrosphere.entities.Address;
import br.com.fiap.gastrosphere.entities.Restaurant;
import br.com.fiap.gastrosphere.exceptions.UnprocessableEntityException;
import br.com.fiap.gastrosphere.services.RestaurantServiceImpl;
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
    public ResponseEntity<List<RestaurantBodyResponse>> findAllRestaurants(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        logger.info("GET | {} | Iniciado findAllRestaurants", V1_RESTAURANT);
        Page<Restaurant> restaurants = this.restaurantService.findAllRestaurants(page, size);
        List<RestaurantBodyResponse> restaurantResponses = restaurants.stream()
                .map(RestaurantBodyResponse::new)
                .toList();
        logger.info("GET | {} | Finalizado findAllRestaurants", V1_RESTAURANT);
        return ok(restaurantResponses);
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
                            description = NOT_FOUND,
                            responseCode = HTTP_STATUS_CODE_404,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantBodyResponse> findRestaurantById(@PathVariable("id") UUID id) {
        logger.info("GET | {} | Iniciado findRestaurantById | id: {}", V1_RESTAURANT, id);
        var restaurant = restaurantService.findById(id);
        if (restaurant != null) {
            logger.info("GET | {} | Finalizado findRestaurantById | id: {}", V1_RESTAURANT, id);
            return ok(new RestaurantBodyResponse(restaurant));
        }
        logger.info("GET | {} | Finalizado √ No Content | id: {}", V1_RESTAURANT, id);
        return status(HttpStatus.NOT_FOUND).build();
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
    public ResponseEntity<UUID> createRestaurant(@Valid @RequestBody RestaurantBodyRequest restaurantBodyRequest) {
        logger.info("POST | {} | Iniciado createRestaurant | Restaurant: {}", V1_RESTAURANT, restaurantBodyRequest.getName());
        Restaurant restaurant = restaurantService.createRestaurant(convertToRestaurant(restaurantBodyRequest));
        logger.info("POST | {} | Finalizado createRestaurant", V1_RESTAURANT);
        return status(201).body(restaurant.getId());
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
    public ResponseEntity<String> updateRestaurant(@PathVariable("id") UUID id, @Valid @RequestBody RestaurantBodyRequest restaurantBodyRequest) {
        logger.info("PUT | {} | Iniciado updateRestaurant | id: {}", V1_RESTAURANT, id);
        restaurantService.updateRestaurant(convertToRestaurant(restaurantBodyRequest), id);
        logger.info("PUT | {} | Finalizado updateRestaurant", V1_RESTAURANT);
        return ok("Restaurante atualizado com sucesso");
    }


    @Operation(
            description = "Exclui restaurante por id.",
            summary = "Exclui restaurante por id.",
            responses = {
                    @ApiResponse(
                            description = NO_CONTENT,
                            responseCode = HTTP_STATUS_CODE_204,
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
    public ResponseEntity<String> deleteRestaurant(@PathVariable("id") UUID id) {
        logger.info("DELETE | {} | Iniciado deleteRestaurant | id: {}", V1_RESTAURANT, id);
        try {
            restaurantService.deleteRestaurantById(id);
            logger.info("DELETE | {} | Restaurante deletado com sucesso | Id: {}", V1_RESTAURANT, id);
            return ResponseEntity.noContent().build();
        } catch (UnprocessableEntityException e) {
            logger.error("DELETE | {} | Erro ao deletar restaurante | Id: {} | Erro: {}", V1_RESTAURANT, id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Restaurante não encontrado");
        }
    }

}
