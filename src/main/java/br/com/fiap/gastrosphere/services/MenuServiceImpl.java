package br.com.fiap.gastrosphere.services;

import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.ERRO_AO_ALTERAR_MENU;
import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.ERRO_AO_CRIAR_MENU;
import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.ERRO_AO_DELETAR_MENU;
import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.ID_NAO_ENCONTRADO;
import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.MENU_NAO_ENCONTRADO;
import static br.com.fiap.gastrosphere.utils.GastroSphereUtils.uuidValidator;
import static java.time.LocalDate.now;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.data.domain.PageRequest.of;

import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import br.com.fiap.gastrosphere.entities.Menu;
import br.com.fiap.gastrosphere.exceptions.ResourceNotFoundException;
import br.com.fiap.gastrosphere.exceptions.UnprocessableEntityException;
import br.com.fiap.gastrosphere.repositories.MenuRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class MenuServiceImpl {
    private static final Logger logger = getLogger(MenuServiceImpl.class);
    private final MenuRepository menuRepository;

    public MenuServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
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
        try {
            return menuRepository.save(menu);
        } catch (DataAccessException e) {
            logger.error(ERRO_AO_CRIAR_MENU, e);
            throw new UnprocessableEntityException(ERRO_AO_CRIAR_MENU);
        }
    }

    public Menu updateMenu(Menu menu, UUID id) {
        Menu existingMenu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MENU_NAO_ENCONTRADO));

        if (menu.getName() != null) existingMenu.setName(menu.getName());
        if (menu.getItemsMenu() != null) existingMenu.setItemsMenu(menu.getItemsMenu());
        existingMenu.setLastModifiedAt(now());

        try {
            return menuRepository.save(existingMenu);
        } catch (DataAccessException e) {
            logger.error(ERRO_AO_ALTERAR_MENU, e);
            throw new UnprocessableEntityException(ERRO_AO_ALTERAR_MENU);
        }
    }

    public void deleteMenuById(UUID id) {
        try {
           menuRepository.deleteById(id);
        } catch (DataAccessException e) {
            logger.error(ERRO_AO_DELETAR_MENU, e);
            throw new UnprocessableEntityException(ERRO_AO_DELETAR_MENU);
        }
    }

}
