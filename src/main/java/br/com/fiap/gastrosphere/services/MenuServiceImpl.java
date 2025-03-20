package br.com.fiap.gastrosphere.services;

import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.ERRO_AO_ALTERAR_MENU;
import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.ERRO_AO_DELETAR_MENU;
import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.ID_NAO_ENCONTRADO;
import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.MENU_NAO_ENCONTRADO;
import static br.com.fiap.gastrosphere.utils.GastroSphereUtils.uuidValidator;
import static java.time.LocalDate.now;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.data.domain.PageRequest.of;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import br.com.fiap.gastrosphere.entities.Menu;
import br.com.fiap.gastrosphere.entities.MenuItem;
import br.com.fiap.gastrosphere.exceptions.ResourceNotFoundException;
import br.com.fiap.gastrosphere.exceptions.UnprocessableEntityException;
import br.com.fiap.gastrosphere.repositories.MenuItemRepository;
import br.com.fiap.gastrosphere.repositories.MenuRepository;
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

    public Page<Menu> findAllMenus(int page, int size) {
        return menuRepository.findAll(of(page, size));
    }
    
    public Menu findById(UUID id) {
        uuidValidator(id);
        return menuRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(ID_NAO_ENCONTRADO));
    }

    public Menu createMenu(Menu menu) {
        return saveMenu(menu);
    }
    
    public Menu updateMenu(Menu menu, UUID id) {
        Menu existingMenu = findExistingMenu(id);
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

    private void updateOrAddItem(List<MenuItem> menuItems, Menu existingMenu) {
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
    
    private Menu findExistingMenu(UUID id) {
        return menuRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(MENU_NAO_ENCONTRADO));
    }
	
    private void updateMenuFields(Menu menu, Menu existingMenu) {
        if (menu.getName() != null) existingMenu.setName(menu.getName());
        existingMenu.setLastModifiedAt(now());
    }

    private void updateMenuItem(MenuItem existingItem, MenuItem incomingItem) {
        if (incomingItem.getDescription() != null) existingItem.setDescription(incomingItem.getDescription());
        if (incomingItem.getPrice() != null) existingItem.setPrice(incomingItem.getPrice());
        if (incomingItem.getIsAvailable() != null) existingItem.setIsAvailable(incomingItem.getIsAvailable());
        if (incomingItem.getImage() != null) existingItem.setImage(incomingItem.getImage());
        existingItem.setLastModifiedAt(now());
        menuItemRepository.save(existingItem);
    }
    
    private void addNewMenuItem(MenuItem newItem, Menu existingMenu) {
        newItem.setMenu(existingMenu);
        menuItemRepository.save(newItem);
    }

    private Menu saveMenu(Menu menu) {
        try {
            return menuRepository.save(menu);
        } catch (DataAccessException e) {
            logger.error(ERRO_AO_ALTERAR_MENU, e);
            throw new UnprocessableEntityException(ERRO_AO_ALTERAR_MENU);
        }
    }
}
