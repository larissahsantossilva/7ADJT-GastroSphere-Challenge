package br.com.fiap.gastrosphere.repositories;

import br.com.fiap.gastrosphere.entities.Restaurant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantRepository {

    List<Restaurant> findAll(int size, int offset);

    Optional<Restaurant> findById(UUID id);

    Optional<Restaurant> findByAddressId(UUID id);

    Optional<Integer> create(Restaurant restaurant);

    Optional<Integer> updateById(UUID id, Restaurant restaurant);

    Optional<Integer> deleteById(UUID id);
}
