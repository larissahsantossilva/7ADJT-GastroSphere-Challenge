package br.com.fiap.gastrosphere.core.application.service;

import static br.com.fiap.gastrosphere.core.application.utils.GastroSphereConstants.ERRO_AO_ALTERAR_MENU;
import static br.com.fiap.gastrosphere.core.application.utils.GastroSphereConstants.ERRO_AO_DELETAR_MENU;
import static br.com.fiap.gastrosphere.core.application.utils.GastroSphereConstants.ID_NAO_ENCONTRADO;
import static br.com.fiap.gastrosphere.core.application.utils.GastroSphereConstants.MENU_NAO_ENCONTRADO;
import static br.com.fiap.gastrosphere.core.application.utils.GastroSphereUtils.uuidValidator;
import static java.time.LocalDate.now;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.data.domain.PageRequest.of;

import java.util.List;
import java.util.UUID;

import br.com.fiap.gastrosphere.core.infra.model.MenuItemModel;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import br.com.fiap.gastrosphere.core.infra.model.MenuModel;
import br.com.fiap.gastrosphere.core.domain.exception.ResourceNotFoundException;
import br.com.fiap.gastrosphere.core.domain.exception.UnprocessableEntityException;
import br.com.fiap.gastrosphere.core.infra.repository.MenuItemRepository;
import br.com.fiap.gastrosphere.core.infra.repository.MenuRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class MenuServiceImpl {
	
    private static final Logger logger = getLogger(MenuServiceImpl.class);
    
    private final MenuRepository menuRepository;
    
    private final MenuItemRepository menuItemRepository;

    public MenuServiceImpl(MenuRepository menuRepository, MenuItemRepository menuItemRepository) {
        this.menuRepository = menuRepository;
        this.menuItemRepository = menuItemRepository;
    }

    public Page<MenuModel> findAllMenus(int page, int size) {
        return menuRepository.findAll(of(page, size));
    }
    
    public MenuModel findById(UUID id) {
        uuidValidator(id);
        return menuRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(ID_NAO_ENCONTRADO));
    }

    public MenuModel createMenu(MenuModel menu) {
        return saveMenu(menu);
    }
    
    public MenuModel updateMenu(MenuModel menu, UUID id) {
        MenuModel existingMenu = findExistingMenu(id);
        updateMenuFields(menu, existingMenu);
        if (menu.getItemsMenu() != null) {
            updateOrAddItem(menu.getItemsMenu(), existingMenu);
        }
        return saveMenu(existingMenu);
    }

    public void deleteMenuById(UUID id) {
        try {
           menuRepository.deleteById(id);
        } catch (DataAccessException e) {
            logger.error(ERRO_AO_DELETAR_MENU, e);
            throw new UnprocessableEntityException(ERRO_AO_DELETAR_MENU);
        }
    }

    public void updateOrAddItem(List<MenuItemModel> menuItems, MenuModel existingMenu) {
         menuItems.stream().forEach(item -> {
        	 if (item.getId() != null) {
                 menuItemRepository.findById(item.getId()).ifPresentOrElse(
                     existingItem -> updateMenuItem(existingItem, item),
                     () -> addNewMenuItem(item, existingMenu)
                 );
             } else {
                 addNewMenuItem(item, existingMenu);
             }
         });
    }
    
    private MenuModel findExistingMenu(UUID id) {
        return menuRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(MENU_NAO_ENCONTRADO));
    }
	
    private void updateMenuFields(MenuModel menu, MenuModel existingMenu) {
        if (menu.getName() != null) existingMenu.setName(menu.getName());
        existingMenu.setLastModifiedAt(now());
    }

    public void updateMenuItem(MenuItemModel existingItem, MenuItemModel incomingItem) {
        if (incomingItem.getDescription() != null) existingItem.setDescription(incomingItem.getDescription());
        if (incomingItem.getPrice() != null) existingItem.setPrice(incomingItem.getPrice());
        if (incomingItem.getIsAvailable() != null) existingItem.setIsAvailable(incomingItem.getIsAvailable());
        if (incomingItem.getImage() != null) existingItem.setImage(incomingItem.getImage());
        existingItem.setLastModifiedAt(now());
        menuItemRepository.save(existingItem);
    }
    
    public void addNewMenuItem(MenuItemModel newItem, MenuModel existingMenu) {
        newItem.setMenu(existingMenu);
        menuItemRepository.save(newItem);
    }

    private MenuModel saveMenu(MenuModel menu) {
        try {
            return menuRepository.save(menu);
        } catch (DataAccessException e) {
            logger.error(ERRO_AO_ALTERAR_MENU, e);
            throw new UnprocessableEntityException(ERRO_AO_ALTERAR_MENU);
        }
    }
}
