package br.com.fiap.gastrosphere.services;

import br.com.fiap.gastrosphere.entities.Restaurant;
import br.com.fiap.gastrosphere.exceptions.ResourceNotFoundException;
import br.com.fiap.gastrosphere.exceptions.UnprocessableEntityException;
import br.com.fiap.gastrosphere.repositories.RestaurantRepository;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.fiap.gastrosphere.utils.GastroSphereConstants.*;
import static br.com.fiap.gastrosphere.utils.GastroSphereUtils.uuidValidator;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class RestaurantServiceImpl {
    private static final Logger logger = getLogger(RestaurantServiceImpl.class);
    private final RestaurantRepository restaurantRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public List<Restaurant> findAllRestaurants(int page, int size) {
        int offset = (page - 1) * size;
        logger.info("size {}, offset {}", size, offset);
        return this.restaurantRepository.findAll(size, offset);
    }

    public Optional<Restaurant> findById(UUID id) {
        uuidValidator(id);
        return this.restaurantRepository.findById(id);
    }

    public void createRestaurant(Restaurant restaurant) {
        Optional<Integer> result;
        try {
            result = this.restaurantRepository.create(restaurant);
            if (result.isPresent() && result.get() != 1) {
                logger.error(ERRO_AO_CRIAR_RESTAURANTE);
                throw new UnprocessableEntityException(ERRO_AO_CRIAR_RESTAURANTE);
            }
        } catch (DataAccessException e) {
            logger.error(ERRO_AO_CRIAR_RESTAURANTE, e);
            throw new UnprocessableEntityException(ERRO_AO_CRIAR_RESTAURANTE);
        }
    }

    public void updateRestaurant(Restaurant restaurant, UUID id) {
        Optional<Integer> result;
        try {
            result = this.restaurantRepository.updateById(id, restaurant);
            if (result.isPresent() && result.get() != 1) {
                logger.error(RESTAURANTE_NAO_ENCONTRADO);
                throw new ResourceNotFoundException(RESTAURANTE_NAO_ENCONTRADO);
            }
        } catch (DataAccessException e) {
            logger.error(ERRO_AO_ALTERAR_RESTAURANTE, e);
            throw new UnprocessableEntityException(ERRO_AO_ALTERAR_RESTAURANTE);
        }
    }


    public void deleteRestaurantById(UUID id) {
        Optional<Integer> result;
        try {
            result = this.restaurantRepository.deleteById(id);
            if (result.isPresent() && result.get() != 1) {
                logger.error(RESTAURANTE_NAO_ENCONTRADO);
                throw new ResourceNotFoundException(RESTAURANTE_NAO_ENCONTRADO);
            }
        } catch (DataAccessException e) {
            logger.error(ERRO_AO_DELETAR_RESTAURANTE, e);
            throw new UnprocessableEntityException(ERRO_AO_DELETAR_RESTAURANTE);
        }
    }

}
