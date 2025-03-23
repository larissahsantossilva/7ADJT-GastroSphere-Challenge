package br.com.fiap.gastrosphere.core.domain.gateway.restauranttype;

import br.com.fiap.gastrosphere.core.domain.entity.RestaurantType;

import java.util.List;
import java.util.UUID;

public interface SearchRestaurantTypeInterface {
    List<RestaurantType> findAll(int page, int size);
    RestaurantType findById(UUID id);
}
