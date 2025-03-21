package br.com.fiap.gastrosphere.core.application.service;

import br.com.fiap.gastrosphere.core.infra.model.Restaurant;
import br.com.fiap.gastrosphere.exceptions.ResourceNotFoundException;
import br.com.fiap.gastrosphere.exceptions.UnprocessableEntityException;
import br.com.fiap.gastrosphere.core.infra.repository.RestaurantRepository;
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
public class RestaurantServiceImpl {
    private static final Logger logger = getLogger(RestaurantServiceImpl.class);
    private final RestaurantRepository restaurantRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public Page<Restaurant> findAllRestaurants(int page, int size) {
        return restaurantRepository.findAll(PageRequest.of(page, size));
    }

    public Restaurant findById(UUID id) {
        uuidValidator(id);
        return restaurantRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(ID_NAO_ENCONTRADO));
    }

    public Restaurant createRestaurant(Restaurant restaurant) {
        try {
            return restaurantRepository.save(restaurant);
        } catch (DataAccessException e) {
            logger.error(ERRO_AO_CRIAR_RESTAURANTE, e);
            throw new UnprocessableEntityException(ERRO_AO_CRIAR_RESTAURANTE);
        }
    }

    public Restaurant updateRestaurant(Restaurant restaurant, UUID id) {
        Restaurant existingRestaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESTAURANTE_NAO_ENCONTRADO));

        if (restaurant.getUser() != null) existingRestaurant.setUser(restaurant.getUser());
        if (restaurant.getName() != null) existingRestaurant.setName(restaurant.getName());
        if (restaurant.getAddress() != null) existingRestaurant.setAddress(restaurant.getAddress());
        if (restaurant.getRestaurantType() != null) existingRestaurant.setRestaurantType(restaurant.getRestaurantType());
        if (restaurant.getStartedAt() != null) existingRestaurant.setStartedAt(restaurant.getStartedAt());
        if (restaurant.getFinishedAt() != null) existingRestaurant.setFinishedAt(restaurant.getFinishedAt());

        existingRestaurant.setLastModifiedAt(LocalDate.now());

        try {
            return restaurantRepository.save(existingRestaurant);
        } catch (DataAccessException e) {
            logger.error(ERRO_AO_ALTERAR_RESTAURANTE, e);
            throw new UnprocessableEntityException(ERRO_AO_ALTERAR_RESTAURANTE);
        }
    }

    public void deleteRestaurantById(UUID id) {
        try {
           restaurantRepository.deleteById(id);
        } catch (DataAccessException e) {
            logger.error(ERRO_AO_DELETAR_RESTAURANTE, e);
            throw new UnprocessableEntityException(ERRO_AO_DELETAR_RESTAURANTE);
        }
    }

}
