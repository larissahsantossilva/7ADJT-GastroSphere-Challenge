package br.com.fiap.gastrosphere.controllers;

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
import static br.com.fiap.gastrosphere.utils.GastroSphereUtils.convertToMenu;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import java.util.List;
import java.util.UUID;

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

import br.com.fiap.gastrosphere.dtos.requests.MenuBodyRequest;
import br.com.fiap.gastrosphere.dtos.responses.MenuBodyResponse;
import br.com.fiap.gastrosphere.core.infra.model.Menu;
import br.com.fiap.gastrosphere.exceptions.UnprocessableEntityException;
import br.com.fiap.gastrosphere.services.MenuServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(MenuController.V1_MENU)
@Tag(name = "MenuController", description = "Controller para CRUD de Menus.")
public class MenuController {

    public static final String V1_MENU = "/api/v1/menus";
    
    private static final Logger logger = getLogger(MenuController.class);
    
    private final MenuServiceImpl menuService;

    public MenuController(MenuServiceImpl menuService) {
        this.menuService = menuService;
    }

    @Operation(
            description = "Busca todos os menus de forma paginada.",
            summary = "Busca todos os menus de forma paginada.",
            responses = {
                    @ApiResponse(
                            description = OK,
                            responseCode = HTTP_STATUS_CODE_200,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Menu.class))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<MenuBodyResponse>> findAllMenus(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        logger.info("GET | {} | Iniciado findAllMenus", V1_MENU);
        Page<Menu> menus = this.menuService.findAllMenus(page, size);
        List<MenuBodyResponse> menuResponses = menus.stream()
                .map(MenuBodyResponse::new)
                .toList();
        logger.info("GET | {} | Finalizado findAllMenus", V1_MENU);
        return ok(menuResponses);
    }

    @Operation(
            description = "Busca Menu por id.",
            summary = "Busca Menu por id.",
            responses = {
                    @ApiResponse(
                            description = OK,
                            responseCode = HTTP_STATUS_CODE_200,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Menu.class))
                    ),
                    @ApiResponse(
                            description = NOT_FOUND,
                            responseCode = HTTP_STATUS_CODE_404,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<MenuBodyResponse> findMenuById(@PathVariable("id") UUID id) {
        logger.info("GET | {} | Iniciado findMenuById | id: {}", V1_MENU, id);
        var menu = menuService.findById(id);
        if (menu != null) {
            logger.info("GET | {} | Finalizado findMenuById | id: {}", V1_MENU, id);
            return ok(new MenuBodyResponse(menu));
        }
        logger.info("GET | {} | Finalizado √ No Content | id: {}", V1_MENU, id);
        return status(HttpStatus.NOT_FOUND).build();
    }


    @Operation(
            description = "Cria Menu.",
            summary = "Cria Menu.",
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
    @PostMapping
    public ResponseEntity<UUID> createMenu(@Valid @RequestBody MenuBodyRequest menuBodyRequest) {
        logger.info("POST | {} | Iniciado createMenu | Menu: {}", V1_MENU, menuBodyRequest.getName());
        Menu menu = menuService.createMenu(convertToMenu(menuBodyRequest));
        logger.info("POST | {} | Finalizado createMenu", V1_MENU);
        return status(201).body(menu.getId());
    }

    @Operation(
            description = "Atualiza Menu por id.",
            summary = "Atualiza Menu por id.",
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
    @PutMapping("/{id}")
    public ResponseEntity<String> updateMenu(@PathVariable("id") UUID id, @Valid @RequestBody MenuBodyRequest menuBodyRequest) {
        logger.info("PUT | {} | Iniciado updateMenu | id: {}", V1_MENU, id);
        menuService.updateMenu(convertToMenu(menuBodyRequest), id);
        logger.info("PUT | {} | Finalizado updateMenu", V1_MENU);
        return ok("Menu atualizado com sucesso");
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
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMenu(@PathVariable("id") UUID id) {
        logger.info("DELETE | {} | Iniciado deleteMenu | id: {}", V1_MENU, id);
        try {
            menuService.deleteMenuById(id);
            logger.info("DELETE | {} | Menu deletado com sucesso | Id: {}", V1_MENU, id);
            return ResponseEntity.noContent().build();
        } catch (UnprocessableEntityException e) {
            logger.error("DELETE | {} | Erro ao deletar Menu | Id: {} | Erro: {}", V1_MENU, id, e.getMessage());
            return status(HttpStatus.NOT_FOUND).body("Menu não encontrado");
        }
    }

}
