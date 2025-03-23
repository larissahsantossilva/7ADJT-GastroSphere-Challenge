package br.com.fiap.gastrosphere.core.application.service;

import static br.com.fiap.gastrosphere.core.application.utils.GastroSphereConstants.ERRO_AO_ALTERAR_MENU_ITEM;
import static br.com.fiap.gastrosphere.core.application.utils.GastroSphereConstants.ERRO_AO_DELETAR_MENU_ITEM;
import static br.com.fiap.gastrosphere.core.application.utils.GastroSphereConstants.ID_NAO_ENCONTRADO;
import static br.com.fiap.gastrosphere.core.application.utils.GastroSphereConstants.ITEM_MENU_NAO_ENCONTRADO;
import static br.com.fiap.gastrosphere.core.application.utils.GastroSphereConstants.MENU_ITEM_NÃO_ENCONTRADO;
import static br.com.fiap.gastrosphere.core.application.utils.GastroSphereConstants.MENU_NÃO_ENCONTRADO_ID;
import static br.com.fiap.gastrosphere.core.application.utils.GastroSphereUtils.uuidValidator;
import static java.time.LocalDate.now;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.data.domain.PageRequest.of;

import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import br.com.fiap.gastrosphere.core.domain.exception.ResourceNotFoundException;
import br.com.fiap.gastrosphere.core.domain.exception.UnprocessableEntityException;
import br.com.fiap.gastrosphere.core.infra.model.MenuItemModel;
import br.com.fiap.gastrosphere.core.infra.model.MenuModel;
import br.com.fiap.gastrosphere.core.infra.repository.MenuItemRepository;
import br.com.fiap.gastrosphere.core.infra.repository.MenuRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class MenuItemServiceImpl {
	
	private static final Logger logger = getLogger(MenuItemServiceImpl.class);
    
    private final MenuItemRepository menuItemRepository;
    
    private final MenuRepository menuRepository;

    public MenuItemServiceImpl(MenuRepository menuRepository, MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
        this.menuRepository = menuRepository;
    }

    public Page<MenuItemModel> findAllMenuItems(int page, int size, UUID menuId) {
        return menuItemRepository.findAllByMenuId(of(page, size), menuId);
    }
    
    public MenuItemModel findByIdAndMenuId(UUID id, UUID menuId) {
    	uuidValidator(id);
    	uuidValidator(menuId);
        return menuItemRepository.findByIdAndMenuId(id, menuId).orElseThrow(() ->
        new ResourceNotFoundException(ID_NAO_ENCONTRADO));
    }

    public MenuItemModel createMenu(MenuItemModel item, UUID menuId) {
    	uuidValidator(menuId);
        MenuModel menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ResourceNotFoundException(MENU_NÃO_ENCONTRADO_ID + menuId));
        item.setMenu(menu);
        return menuItemRepository.save(item);
    }
    
    public MenuItemModel updateMenuItem(MenuItemModel item, UUID menuId, UUID menuItemId) {
        MenuItemModel existingMenuItem = menuItemRepository.findByIdAndMenuId(menuItemId, menuId)
                .orElseThrow(() -> new ResourceNotFoundException(ITEM_MENU_NAO_ENCONTRADO));
        if (item.getIsAvailable() != null) existingMenuItem.setIsAvailable(item.getIsAvailable());
        if (item.getDescription() != null) existingMenuItem.setDescription(item.getDescription());
        if (item.getImage() != null) existingMenuItem.setImage(item.getImage());
        if (item.getMenu() != null) existingMenuItem.setMenu(item.getMenu());
        if (item.getPrice() != null) existingMenuItem.setPrice(item.getPrice());
        existingMenuItem.setLastModifiedAt(now());
        try {
            return menuItemRepository.save(existingMenuItem);
        } catch (DataAccessException e) {
            logger.error(ERRO_AO_ALTERAR_MENU_ITEM, e);
            throw new UnprocessableEntityException(ERRO_AO_ALTERAR_MENU_ITEM);
        }
    }
    
    public void deleteMenuById(UUID menuId, UUID menuItemId) {
        try {
            var existingItem = menuItemRepository.findByIdAndMenuId(menuItemId, menuId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            MENU_ITEM_NÃO_ENCONTRADO + menuItemId));
            menuItemRepository.deleteById(existingItem.getId());
        } catch (DataAccessException e) {
            logger.error(ERRO_AO_DELETAR_MENU_ITEM, e);
            throw new UnprocessableEntityException(ERRO_AO_DELETAR_MENU_ITEM);
        }
    }

}
