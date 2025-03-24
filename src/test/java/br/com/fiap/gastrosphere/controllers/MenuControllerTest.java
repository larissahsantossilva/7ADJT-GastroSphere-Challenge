package br.com.fiap.gastrosphere.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.fiap.gastrosphere.core.application.controller.MenuController;
import br.com.fiap.gastrosphere.core.application.dto.request.MenuBodyRequest;
import br.com.fiap.gastrosphere.core.application.dto.response.MenuBodyResponse;
import br.com.fiap.gastrosphere.core.application.service.MenuServiceImpl;
import br.com.fiap.gastrosphere.core.domain.exception.UnprocessableEntityException;
import br.com.fiap.gastrosphere.core.infra.model.MenuModel;

class MenuControllerTest {

    private MenuServiceImpl menuService;
    
    private MenuController controller;

    @BeforeEach
    void setUp() {
        menuService = mock(MenuServiceImpl.class);
        controller = new MenuController(menuService);
    }

    @Test
    void deveListarTodosMenus() {
        Page<MenuModel> page = new PageImpl<>(Collections.singletonList(new MenuModel()));
        when(menuService.findAllMenus(0, 10)).thenReturn(page);

        ResponseEntity<List<MenuBodyResponse>> response = controller.findAllMenus(0, 10);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        verify(menuService).findAllMenus(0, 10);
    }

    @Test
    void deveBuscarMenuPorId_Encontrado() {
        UUID id = UUID.randomUUID();
        MenuModel menu = new MenuModel();
        when(menuService.findById(id)).thenReturn(menu);

        ResponseEntity<MenuBodyResponse> response = controller.findMenuById(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        verify(menuService).findById(id);
    }

    @Test
    void deveRetornar404_QuandoMenuNaoEncontrado() {
        UUID id = UUID.randomUUID();
        when(menuService.findById(id)).thenReturn(null);

        ResponseEntity<MenuBodyResponse> response = controller.findMenuById(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void deveCriarMenu_ComSucesso() {
        MenuBodyRequest request = new MenuBodyRequest();
        UUID id = UUID.randomUUID();
        MenuModel menu = new MenuModel();
        menu.setId(id);

        when(menuService.createMenu(any())).thenReturn(menu);

        ResponseEntity<UUID> response = controller.createMenu(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(id);
        verify(menuService).createMenu(any());
    }

    @Test
    void deveAtualizarMenu_ComSucesso() {
        UUID id = UUID.randomUUID();
        MenuBodyRequest request = new MenuBodyRequest();
        MenuModel updatedMenu = new MenuModel();
        updatedMenu.setId(id);
        updatedMenu.setName("Menu Atualizado");

        // Simulando o retorno do método updateMenu
        when(menuService.updateMenu(any(MenuModel.class), eq(id))).thenReturn(updatedMenu);

        // Chamando o método do controller
        ResponseEntity<String> response = controller.updateMenu(id, request);

        // Verificando a resposta
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Menu atualizado com sucesso");

        // Verificando que o serviço foi chamado corretamente
        verify(menuService).updateMenu(any(MenuModel.class), eq(id));
    }

    
    @Test
    void deveExcluirMenu_ComSucesso() {
        UUID id = UUID.randomUUID();
        doNothing().when(menuService).deleteMenuById(id);

        ResponseEntity<String> response = controller.deleteMenu(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
        verify(menuService).deleteMenuById(id);
    }

    @Test
    void deveRetornarErro_AoExcluirMenuNaoEncontrado() {
        UUID id = UUID.randomUUID();
        doThrow(new UnprocessableEntityException("Menu não encontrado"))
                .when(menuService).deleteMenuById(id);

        ResponseEntity<String> response = controller.deleteMenu(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Menu não encontrado");
    }
}
