package br.com.fiap.gastrosphere.services;

import br.com.fiap.gastrosphere.entities.RestaurantType;
import br.com.fiap.gastrosphere.exceptions.ResourceNotFoundException;
import br.com.fiap.gastrosphere.exceptions.UnprocessableEntityException;
import br.com.fiap.gastrosphere.repositories.RestaurantTypeRepository;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.*;
import static br.com.fiap.gastrosphere.utils.GastroSphereUtils.uuidValidator;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class RestaurantTypeServiceImpl {
    private static final Logger logger = getLogger(RestaurantTypeServiceImpl.class);
    private final RestaurantTypeRepository restaurantTypeRepository;


    public RestaurantTypeServiceImpl(RestaurantTypeRepository restaurantTypeRepository) {
        this.restaurantTypeRepository = restaurantTypeRepository;
    }

    public Page<RestaurantType> findAllRestaurantTypes(int page, int size) {
        return restaurantTypeRepository.findAll(PageRequest.of(page, size));
    }

    public RestaurantType findById(UUID id) {
        uuidValidator(id);
        return restaurantTypeRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(ID_NAO_ENCONTRADO));
    }

    public RestaurantType createRestaurantType(RestaurantType restaurantType) {
        try {
            return restaurantTypeRepository.save(restaurantType);
        } catch (DataAccessException e) {
            logger.error(ERRO_AO_CRIAR_TIPO_RESTAURANTE, e);
            throw new UnprocessableEntityException(ERRO_AO_CRIAR_TIPO_RESTAURANTE);
        }
    }

    public RestaurantType updateRestaurantType(RestaurantType restaurantType, UUID id) {
        RestaurantType existingRestaurantType = restaurantTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(TIPO_RESTAURANTE_NAO_ENCONTRADO));

        if (restaurantType.getName() != null) existingRestaurantType.setName(restaurantType.getName());

        existingRestaurantType.setLastModifiedAt(LocalDate.now());

        try {
            return restaurantTypeRepository.save(existingRestaurantType);
        } catch (DataAccessException e) {
            logger.error(ERRO_AO_ALTERAR_TIPO_RESTAURANTE, e);
            throw new UnprocessableEntityException(ERRO_AO_ALTERAR_TIPO_RESTAURANTE);
        }
    }

    public void deleteRestaurantTypeById(UUID id) {
        try {
            restaurantTypeRepository.deleteById(id);
        } catch (DataAccessException e) {
            logger.error(ERRO_AO_DELETAR_TIPO_RESTAURANTE, e);
            throw new UnprocessableEntityException(ERRO_AO_DELETAR_TIPO_RESTAURANTE);
        }
    }

}
