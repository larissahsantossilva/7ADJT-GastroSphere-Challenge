package br.com.fiap.gastrosphere.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import br.com.fiap.gastrosphere.core.application.service.MenuItemServiceImpl;
import br.com.fiap.gastrosphere.core.domain.exception.ResourceNotFoundException;
import br.com.fiap.gastrosphere.core.infra.model.MenuItemModel;
import br.com.fiap.gastrosphere.core.infra.model.MenuModel;
import br.com.fiap.gastrosphere.core.infra.repository.MenuItemRepository;
import br.com.fiap.gastrosphere.core.infra.repository.MenuRepository;

class MenuItemServiceImplTest {

    @Mock
    private MenuItemRepository menuItemRepo;

    @Mock
    private MenuRepository menuRepo;
    
    private MenuItemServiceImpl service;

    private AutoCloseable mock;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        service = new MenuItemServiceImpl(menuRepo, menuItemRepo);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Test
    void deveBuscarTodosItensPorMenuId() {
        UUID menuId = UUID.randomUUID();
        var item = new MenuItemModel();
        item.setId(UUID.randomUUID());
        item.setDescription("Item Especial");

        Page<MenuItemModel> page = new PageImpl<>(List.of(item), Pageable.unpaged(), 1);

        when(menuItemRepo.findAllByMenuId(any(Pageable.class), eq(menuId))).thenReturn(page);

        var items = service.findAllMenuItems(0, 10, menuId);

        assertThat(items).isNotNull();
        assertThat(items.getTotalElements()).isEqualTo(1);
        verify(menuItemRepo, times(1)).findAllByMenuId(any(Pageable.class), eq(menuId));
    }
    
    @Test
    void deveBuscarMenuItemPorIdEMenuId() {
        UUID menuId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        var item = new MenuItemModel();
        item.setId(itemId);
        item.setMenu(new MenuModel());

        when(menuItemRepo.findByIdAndMenuId(itemId, menuId)).thenReturn(Optional.of(item));

        var foundItem = service.findByIdAndMenuId(itemId, menuId);

        verify(menuItemRepo, times(1)).findByIdAndMenuId(itemId, menuId);
        assertThat(foundItem).isEqualTo(item);
    }

    @Test
    void deveLancarExcecao_QuandoMenuItemNaoEncontradoPorIdEMenuId() {
        UUID menuId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        when(menuItemRepo.findByIdAndMenuId(itemId, menuId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findByIdAndMenuId(itemId, menuId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("ID não encontrado");
    }

    @Test
    void deveSalvarMenuItem_ComMenuAssociado() {
        UUID menuId = UUID.randomUUID();
        var item = new MenuItemModel();
        var menu = new MenuModel();
        menu.setId(menuId);

        when(menuRepo.findById(menuId)).thenReturn(Optional.of(menu));
        when(menuItemRepo.save(any(MenuItemModel.class))).thenAnswer(i -> i.getArgument(0));

        var savedItem = service.createMenu(item, menuId);

        assertThat(savedItem).isNotNull();
        assertThat(savedItem.getMenu()).isEqualTo(menu);
        verify(menuItemRepo).save(any(MenuItemModel.class));
        verify(menuRepo).findById(menuId);
    }

    @Test
    void deveLancarExcecao_AoSalvarMenuItemComMenuNaoEncontrado() {
        UUID menuId = UUID.randomUUID();
        var item = new MenuItemModel();

        when(menuRepo.findById(menuId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createMenu(item, menuId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Menu não encontrado");
    }

    @Test
    void deveAtualizarMenuItem_ComSucesso() {
        UUID menuId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        var existingItem = new MenuItemModel();
        existingItem.setId(itemId);
        var updatedItem = new MenuItemModel();
        updatedItem.setId(itemId);
        updatedItem.setDescription("Item Atualizado");

        when(menuItemRepo.findByIdAndMenuId(itemId, menuId)).thenReturn(Optional.of(existingItem));
        when(menuItemRepo.save(any(MenuItemModel.class))).thenAnswer(i -> i.getArgument(0));

        var result = service.updateMenuItem(updatedItem, menuId, itemId);

        assertThat(result.getDescription()).isEqualTo("Item Atualizado");
        verify(menuItemRepo).findByIdAndMenuId(itemId, menuId);
        verify(menuItemRepo).save(any(MenuItemModel.class));
    }

    @Test
    void deveLancarExcecao_AoAtualizarMenuItemNaoEncontrado() {
        UUID menuId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        var updatedItem = new MenuItemModel();

        when(menuItemRepo.findByIdAndMenuId(itemId, menuId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateMenuItem(updatedItem, menuId, itemId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Item do menu não encontrado");
    }

    @Test
    void deveExcluirMenuItem_ComSucesso() {
        UUID menuId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        var item = new MenuItemModel();
        item.setId(itemId);

        when(menuItemRepo.findByIdAndMenuId(itemId, menuId)).thenReturn(Optional.of(item));
        doNothing().when(menuItemRepo).deleteById(itemId);

        service.deleteMenuById(menuId, itemId);

        verify(menuItemRepo).deleteById(itemId);
        verify(menuItemRepo).findByIdAndMenuId(itemId, menuId);
    }

    @Test
    void deveLancarExcecao_AoExcluirMenuItemNaoEncontrado() {
        UUID menuId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        when(menuItemRepo.findByIdAndMenuId(itemId, menuId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteMenuById(menuId, itemId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Menu Item não encontrado");
    }
}
