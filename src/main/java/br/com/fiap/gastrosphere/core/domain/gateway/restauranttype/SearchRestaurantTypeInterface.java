package br.com.fiap.gastrosphere.core.domain.gateway.restauranttype;

import br.com.fiap.gastrosphere.core.domain.entity.RestaurantType;

import java.util.List;

public interface SearchRestaurantTypeInterface {
    List<RestaurantType> findAll(int page, int size);
}
