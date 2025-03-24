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

import br.com.fiap.gastrosphere.core.application.controller.MenuItemController;
import br.com.fiap.gastrosphere.core.application.dto.request.MenuItemBodyRequest;
import br.com.fiap.gastrosphere.core.application.dto.response.MenuItemResponse;
import br.com.fiap.gastrosphere.core.application.service.MenuItemServiceImpl;
import br.com.fiap.gastrosphere.core.domain.exception.UnprocessableEntityException;
import br.com.fiap.gastrosphere.core.infra.model.MenuItemModel;
import br.com.fiap.gastrosphere.core.infra.model.MenuModel;

class MenuItemControllerTest {

    private MenuItemServiceImpl menuItemService;
    private MenuItemController controller;

    @BeforeEach
    void setUp() {
        menuItemService = mock(MenuItemServiceImpl.class);
        controller = new MenuItemController(menuItemService);
    }

    @Test
    void deveBuscarTodosItensDoMenu() {
        UUID menuId = UUID.randomUUID();

        MenuModel menu = new MenuModel();
        menu.setId(menuId);

        MenuItemModel menuItem = new MenuItemModel();
        menuItem.setId(UUID.randomUUID());
        menuItem.setMenu(menu);

        Page<MenuItemModel> page = new PageImpl<>(Collections.singletonList(menuItem));

        when(menuItemService.findAllMenuItems(0, 10, menuId)).thenReturn(page);
        ResponseEntity<List<MenuItemResponse>> response = controller.findAllMenuItems(menuId, 0, 10);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);

        verify(menuItemService).findAllMenuItems(0, 10, menuId);
    }



    @Test
    void deveBuscarItemDoMenuPorId_Encontrado() {
        UUID menuId = UUID.randomUUID();
        UUID menuItemId = UUID.randomUUID();

        MenuModel menu = new MenuModel();
        menu.setId(menuId);

        MenuItemModel menuItem = new MenuItemModel();
        menuItem.setId(menuItemId);
        menuItem.setMenu(menu); 
        when(menuItemService.findByIdAndMenuId(menuItemId, menuId)).thenReturn(menuItem);

        ResponseEntity<MenuItemResponse> response = controller.findMenuItemById(menuItemId, menuId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        verify(menuItemService).findByIdAndMenuId(menuItemId, menuId);
    }


    @Test
    void deveRetornar404_QuandoItemDoMenuNaoEncontrado() {
        UUID menuId = UUID.randomUUID();
        UUID menuItemId = UUID.randomUUID();
        when(menuItemService.findByIdAndMenuId(menuItemId, menuId)).thenReturn(null);

        ResponseEntity<MenuItemResponse> response = controller.findMenuItemById(menuItemId, menuId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void deveCriarItemDoMenu_ComSucesso() {
        UUID menuId = UUID.randomUUID();
        UUID menuItemId = UUID.randomUUID();
        MenuItemBodyRequest request = new MenuItemBodyRequest();
        MenuItemModel menuItem = new MenuItemModel();
        menuItem.setId(menuItemId);

        when(menuItemService.createMenu(any(MenuItemModel.class), eq(menuId))).thenReturn(menuItem);

        ResponseEntity<UUID> response = controller.createMenuItem(menuId, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(menuItemId);

        verify(menuItemService).createMenu(any(MenuItemModel.class), eq(menuId));
    }

    @Test
    void deveAtualizarItemDoMenu_ComSucesso() {
        UUID menuId = UUID.randomUUID();
        UUID menuItemId = UUID.randomUUID();
        MenuItemBodyRequest request = new MenuItemBodyRequest();
        request.setId(menuItemId);
        request.setMenuId(menuId);

        MenuItemModel updatedItem = new MenuItemModel();
        updatedItem.setId(menuItemId);

        when(menuItemService.updateMenuItem(any(MenuItemModel.class), eq(menuId), eq(menuItemId))).thenReturn(updatedItem);

        ResponseEntity<String> response = controller.updateMenuItem(menuId, menuItemId, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Item do Menu atualizado com sucesso");

        verify(menuItemService).updateMenuItem(any(MenuItemModel.class), eq(menuId), eq(menuItemId));
    }

    @Test
    void deveExcluirItemDoMenu_ComSucesso() {
        UUID menuId = UUID.randomUUID();
        UUID menuItemId = UUID.randomUUID();

        doNothing().when(menuItemService).deleteMenuById(menuId, menuItemId);

        ResponseEntity<String> response = controller.deleteMenuItem(menuId, menuItemId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();

        verify(menuItemService).deleteMenuById(menuId, menuItemId);
    }

    @Test
    void deveRetornarErro_AoExcluirItemDoMenuNaoEncontrado() {
        UUID menuId = UUID.randomUUID();
        UUID menuItemId = UUID.randomUUID();

        doThrow(new UnprocessableEntityException("Item não encontrado")).when(menuItemService).deleteMenuById(menuId, menuItemId);

        ResponseEntity<String> response = controller.deleteMenuItem(menuId, menuItemId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Item do Menu não encontrado");
    }
}
