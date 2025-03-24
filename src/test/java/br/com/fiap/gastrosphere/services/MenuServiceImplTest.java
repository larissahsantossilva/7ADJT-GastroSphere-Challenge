package br.com.fiap.gastrosphere.services;

import static br.com.fiap.gastrosphere.core.application.utils.GastroSphereConstants.ERRO_AO_ALTERAR_MENU;
import static br.com.fiap.gastrosphere.helper.MenuHelper.gerarMenu;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import br.com.fiap.gastrosphere.core.application.service.MenuServiceImpl;
import br.com.fiap.gastrosphere.core.domain.exception.ResourceNotFoundException;
import br.com.fiap.gastrosphere.core.domain.exception.UnprocessableEntityException;
import br.com.fiap.gastrosphere.core.infra.model.MenuItemModel;
import br.com.fiap.gastrosphere.core.infra.model.MenuModel;
import br.com.fiap.gastrosphere.core.infra.repository.MenuItemRepository;
import br.com.fiap.gastrosphere.core.infra.repository.MenuRepository;

class MenuServiceImplTest {

	@Mock
	private MenuRepository menuRepo;

	@Mock
	private MenuItemRepository menuItemRepository;

	@Mock
	private MenuServiceImpl service;
	
	@InjectMocks
    private MenuServiceImpl menuServiceImpl; 
	
	AutoCloseable mock;
	
	@BeforeEach
	void setUp() {
		mock = MockitoAnnotations.openMocks(this);
		service = new MenuServiceImpl(menuRepo, menuItemRepository);
	}
	
	@AfterEach
	void teardown() throws Exception {
		mock.close();
	}
	
    @Test
    void shouldThrowUnprocessableEntityExceptionWhenDataAccessExceptionOccurs() {
        MenuModel menu = new MenuModel();
        when(menuRepo.save(any(MenuModel.class))).thenThrow(new DataIntegrityViolationException("DB error"));

        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class, () -> {
            service.createMenu(menu);
        });

        assertEquals(ERRO_AO_ALTERAR_MENU, exception.getMessage());
        verify(menuRepo, times(1)).save(menu);
    }
	
	@Test
	void devePermitirSalvarMenu() {
		var menu = gerarMenu();
		menu.setId(randomUUID());
		when(menuRepo.save(any(MenuModel.class))).thenAnswer(i -> i.getArgument(0));
		var menuRegistrado = service.createMenu(menu);
		assertThat(menuRegistrado).isNotNull().isInstanceOf(MenuModel.class);
		assertThat(menuRegistrado.getId()).isNotNull();
		assertThat(menuRegistrado.getName()).isEqualTo(menu.getName());
		assertThat(menuRegistrado.getItemsMenu()).isEqualTo(menu.getItemsMenu());
	}
	
	@Test
	void devePermitirPegarMenuPorId() {
		var id = randomUUID();
		var menu = gerarMenu();
		menu.setId(id);
		when(menuRepo.findById(any(UUID.class))).thenReturn(of(menu));
		var menuEncontrado = service.findById(id);
		verify(menuRepo, times(1)).findById(id);
		assertThat(menuEncontrado).isEqualTo(menu);
	}
	
	@Test
    void deveLancarExcecao_QuandoMenuNaoEncontrado() {
        var id = randomUUID();
        when(menuRepo.findById(id)).thenReturn(empty());

        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("ID não encontrado");
    }

    @Test
    void deveAtualizarMenu_ComSucesso() {
        var id = randomUUID();
        var existingMenu = gerarMenu();
        var updatedMenu = gerarMenu();
        existingMenu.setId(id);

        when(menuRepo.findById(eq(id))).thenReturn(of(existingMenu));
        when(menuRepo.save(any(MenuModel.class))).thenAnswer(i -> i.getArgument(0));

        var result = service.updateMenu(updatedMenu, id);

        verify(menuRepo, times(1)).findById(eq(id));
        verify(menuRepo, times(1)).save(any(MenuModel.class));
        assertThat(result.getName()).isEqualTo(updatedMenu.getName());
    }
    
    @Test
    void shouldAddNewMenuItemWhenIdIsNull() {
        MenuItemModel newItem = new MenuItemModel();
        MenuModel existingMenu = new MenuModel();

        menuServiceImpl.updateOrAddItem(List.of(newItem), existingMenu);

        verify(menuItemRepository, never()).findById(any());
    }

    @Test
    void deveExcluirMenu_ComSucesso() {
        var id = randomUUID();
        doNothing().when(menuRepo).deleteById(id);

        service.deleteMenuById(id);

        verify(menuRepo, times(1)).deleteById(id);
    }

    @Test
    void deveLancarErro_AoDeletarMenuNaoEncontrado() {
        var id = randomUUID();
        doThrow(new DataAccessException("Erro ao deletar") {}).when(menuRepo).deleteById(id);

        assertThatThrownBy(() -> service.deleteMenuById(id))
                .isInstanceOf(UnprocessableEntityException.class)
                .hasMessageContaining("Erro ao deletar Menu.");
    }

    @Test
    void deveBuscarTodosMenus() {
        var menu = gerarMenu();
        var page = new PageImpl<>(List.of(menu));
        when(menuRepo.findAll(any(org.springframework.data.domain.Pageable.class))).thenReturn(page);

        Page<MenuModel> menusEncontrados = service.findAllMenus(0, 10);

        assertThat(menusEncontrados).isNotNull();
        assertThat(menusEncontrados.getTotalElements()).isEqualTo(1);
        verify(menuRepo, times(1)).findAll(any(org.springframework.data.domain.Pageable.class));
    }

    @Test
    void deveAtualizarItensDoMenu() {
        var existingMenu = gerarMenu();
        var item = new MenuItemModel();
        item.setId(randomUUID());
        existingMenu.setItemsMenu(List.of(item));

        when(menuItemRepository.findById(any(UUID.class))).thenReturn(of(item));
        when(menuRepo.findById(any())).thenReturn(of(existingMenu));
        when(menuRepo.save(any(MenuModel.class))).thenAnswer(i -> i.getArgument(0));

        var updatedMenu = service.updateMenu(existingMenu, existingMenu.getId());

        assertThat(updatedMenu.getItemsMenu()).contains(item);
        verify(menuItemRepository, times(1)).findById(item.getId());
        verify(menuRepo, times(1)).save(existingMenu);
    }

    @Test
    void deveGerarExcecaoSalvarMenu() {
        var menu = gerarMenu();

        when(menuRepo.save(any())).thenThrow(new DataAccessException("Erro ao salvar") {});

        assertThrows(UnprocessableEntityException.class, () -> {
            service.createMenu(menu);
        });
    }
}
