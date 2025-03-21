package br.com.fiap.gastrosphere.core.application.controller;

import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.ERRO_AO_ALTERAR_MENU;
import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.ERRO_AO_CRIAR_MENU;
import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.HTTP_STATUS_CODE_200;
import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.HTTP_STATUS_CODE_201;
import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.HTTP_STATUS_CODE_204;
import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.HTTP_STATUS_CODE_404;
import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.HTTP_STATUS_CODE_422;
import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.MENU_CRIADO_COM_SUCESSO;
import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.MENU_NAO_ENCONTRADO;
import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.NOT_FOUND;
import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.NO_CONTENT;
import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.OK;
import static br.com.fiap.gastrosphere.utils.GastroSphereUtils.convertToMenuItem;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import java.util.List;
import java.util.UUID;

import br.com.fiap.gastrosphere.core.infra.model.MenuItem;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.gastrosphere.core.application.dto.request.MenuItemBodyRequest;
import br.com.fiap.gastrosphere.core.application.dto.response.MenuItemResponse;
import br.com.fiap.gastrosphere.exceptions.UnprocessableEntityException;
import br.com.fiap.gastrosphere.core.application.service.MenuItemServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(MenuItemController.V1_MENU_ITEMS)
@Tag(name = "MenuItemController", description = "Controller para CRUD de Itens do Menu.")
public class MenuItemController {

    public static final String V1_MENU_ITEMS = "/api/v1/menu";
    
    private static final Logger logger = getLogger(MenuItemController.class);
    
    private final MenuItemServiceImpl menuItemService;

    public MenuItemController(MenuItemServiceImpl menuItemService) {
        this.menuItemService = menuItemService;
    }

    @Operation(
            description = "Busca todos os itens do menu de forma paginada.",
            summary = "Busca todos os itens do menu de forma paginada.",
            responses = {
                    @ApiResponse(
                            description = OK,
                            responseCode = HTTP_STATUS_CODE_200,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MenuItem.class))
                    )
            }
    )
    @GetMapping("/items")
    public ResponseEntity<List<MenuItemResponse>> findAllMenuItems(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        logger.info("GET | {} | Iniciado findAllMenuItems", V1_MENU_ITEMS);
        Page<MenuItem> items = this.menuItemService.findAllMenuItems(page, size);
        List<MenuItemResponse> itemsResponse = items.stream()
                .map(MenuItemResponse::new)
                .toList();
        logger.info("GET | {} | Finalizado findAllMenuItems", V1_MENU_ITEMS);
        return ok(itemsResponse);
    }

    @Operation(
            description = "Busca Item do Menu por id.",
            summary = "Busca Item do Menu por id.",
            responses = {
                    @ApiResponse(
                            description = OK,
                            responseCode = HTTP_STATUS_CODE_200,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MenuItem.class))
                    ),
                    @ApiResponse(
                            description = NOT_FOUND,
                            responseCode = HTTP_STATUS_CODE_404,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
                    )
            }
    )
    @GetMapping("/{id}/items")
    public ResponseEntity<MenuItemResponse> findMenuItemById(@PathVariable("id") UUID id) {
        logger.info("GET | {} | Iniciado findMenuItemById | id: {}", V1_MENU_ITEMS, id);
        var item = menuItemService.findById(id);
        if (item != null) {
            logger.info("GET | {} | Finalizado findMenuItemById | id: {}", V1_MENU_ITEMS, id);
            return ok(new MenuItemResponse(item));
        }
        logger.info("GET | {} | Finalizado √ No Content | id: {}", V1_MENU_ITEMS, id);
        return status(HttpStatus.NOT_FOUND).build();
    }


    @Operation(
            description = "Cria Item do Menu.",
            summary = "Cria Item do Menu.",
            responses = {
                    @ApiResponse(
                            description = MENU_CRIADO_COM_SUCESSO,
                            responseCode = HTTP_STATUS_CODE_201,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(
                            description = ERRO_AO_CRIAR_MENU,
                            responseCode = HTTP_STATUS_CODE_422,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    ),
            }
    )
    @PostMapping("/items")
    public ResponseEntity<UUID> createMenuItem(@Valid @RequestBody MenuItemBodyRequest menuItemBodyRequest) {
        logger.info("POST | {} | Iniciado createMenu | Menu: {}", V1_MENU_ITEMS, menuItemBodyRequest.getDescription());
        MenuItem item = menuItemService.createMenu(convertToMenuItem(menuItemBodyRequest));
        logger.info("POST | {} | Finalizado createMenu", V1_MENU_ITEMS);
        return status(201).body(item.getId());
    }

    @Operation(
            description = "Atualiza Item do Menu por id.",
            summary = "Atualiza Item do Menu por id.",
            responses = {
                    @ApiResponse(
                            description = OK,
                            responseCode = HTTP_STATUS_CODE_200,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
                    ),
                    @ApiResponse(
                            description = MENU_NAO_ENCONTRADO,
                            responseCode = HTTP_STATUS_CODE_404,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(
                            description =  ERRO_AO_ALTERAR_MENU,
                            responseCode = HTTP_STATUS_CODE_422,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    )
            }
    )
    @PutMapping("/{id}/items")
    public ResponseEntity<String> updateMenuItem(@PathVariable("id") UUID id, @Valid @RequestBody MenuItemBodyRequest menuItemBodyRequest) {
        logger.info("PUT | {} | Iniciado updateMenuItem | id: {}", V1_MENU_ITEMS, id);
        menuItemService.updateMenuItem(convertToMenuItem(menuItemBodyRequest), id);
        logger.info("PUT | {} | Finalizado updateMenuItem", V1_MENU_ITEMS);
        return ok("Item do Menu atualizado com sucesso");
    }


    @Operation(
            description = "Exclui Menu por id.",
            summary = "Exclui Menu por id.",
            responses = {
                    @ApiResponse(
                            description = NO_CONTENT,
                            responseCode = HTTP_STATUS_CODE_204,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
                    ),
                    @ApiResponse(
                            description = MENU_NAO_ENCONTRADO,
                            responseCode = HTTP_STATUS_CODE_404,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    )
            }
    )
    @DeleteMapping("/{id}/items")
    public ResponseEntity<String> deleteMenuItem(@PathVariable("id") UUID id) {
        logger.info("DELETE | {} | Iniciado deleteMenuItem | id: {}", V1_MENU_ITEMS, id);
        try {
            menuItemService.deleteMenuById(id);
            logger.info("DELETE | {} | Menu Item deletado com sucesso | Id: {}", V1_MENU_ITEMS, id);
            return noContent().build();
        } catch (UnprocessableEntityException e) {
            logger.error("DELETE | {} | Erro ao deletar Menu Item | Id: {} | Erro: {}", V1_MENU_ITEMS, id, e.getMessage());
            return status(HttpStatus.NOT_FOUND).body("Item do Menu não encontrado");
        }
    }

}
