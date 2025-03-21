package br.com.fiap.gastrosphere.core.application.service;

import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.ERRO_AO_ALTERAR_MENU_ITEM;
import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.ERRO_AO_DELETAR_MENU_ITEM;
import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.ID_NAO_ENCONTRADO;
import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.ITEM_MENU_NAO_ENCONTRADO;
import static br.com.fiap.gastrosphere.utils.GastroSphereUtils.uuidValidator;
import static java.time.LocalDate.now;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.data.domain.PageRequest.of;

import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import br.com.fiap.gastrosphere.core.infra.model.MenuItem;
import br.com.fiap.gastrosphere.exceptions.ResourceNotFoundException;
import br.com.fiap.gastrosphere.exceptions.UnprocessableEntityException;
import br.com.fiap.gastrosphere.core.infra.repository.MenuItemRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class MenuItemServiceImpl {
	
    private static final Logger logger = getLogger(MenuItemServiceImpl.class);
    
    private final MenuItemRepository menuItemRepository;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public Page<MenuItem> findAllMenuItems(int page, int size) {
        return menuItemRepository.findAll(of(page, size));
    }
    
    public MenuItem findById(UUID id) {
        uuidValidator(id);
        return menuItemRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(ID_NAO_ENCONTRADO));
    }

    public MenuItem createMenu(MenuItem item) {
        return menuItemRepository.save(item);
    }
    
    public MenuItem updateMenuItem(MenuItem item, UUID id) {
        MenuItem existingMenuItem = menuItemRepository.findById(id)
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
    

    public void deleteMenuById(UUID id) {
        try {
        	menuItemRepository.deleteById(id);
        } catch (DataAccessException e) {
            logger.error(ERRO_AO_DELETAR_MENU_ITEM, e);
            throw new UnprocessableEntityException(ERRO_AO_DELETAR_MENU_ITEM);
        }
    }

}
