package br.com.fiap.gastrosphere.repositories.impl;

import br.com.fiap.gastrosphere.entities.Restaurant;
import br.com.fiap.gastrosphere.repositories.RestaurantRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final JdbcClient jdbcClient;


    public RestaurantRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }


    @Override
    public List<Restaurant> findAll(int size, int offset) {
        return this.jdbcClient
                .sql("SELECT * FROM gastrosphere.restaurants LIMIT :size OFFSET :offset")
                .param("size", size)
                .param("offset", offset)
                .query(Restaurant.class)
                .list();
    }

    @Override
    public Optional<Restaurant> findById(UUID id) {
        return this.jdbcClient.sql("SELECT * FROM gastrosphere.restaurants WHERE id = :id")
                .param("id", id)
                .query(Restaurant.class)
                .optional();
    }

    @Override
    public Optional<Restaurant> findByAddressId(UUID id) {
        return this.jdbcClient.sql("SELECT * FROM gastrosphere.restaurants r " +
                "INNER JOIN gastrosphere.addresses a ON r.address_id  = a.id " +
                "WHERE a.id = :id")
                .param("id", id)
                .query(Restaurant.class)
                .optional();    }

    @Override
    public Optional<Integer> create(Restaurant restaurant) {
        return Optional.of(this.jdbcClient.sql("INSERT INTO gastrosphere.restaurants (name, address_id, address_number, address_complement, restaurant_type, opening_hours, owner_id) " +
                        "VALUES (:name, :address_id, :address_number, :address_complement, :restaurant_type, :opening_hours, :owner_id)")
                .param("name", restaurant.getName())
                .param("address_id", restaurant.getAddressId())
                .param("address_number", restaurant.getAddressNumber())
                .param("address_complement", restaurant.getAddressComplement())
                .param("restaurant_type", restaurant.getRestaurantType())
                .param("opening_hours", restaurant.getOpeningHours())
                .param("owner_id", restaurant.getOwnerId())
                .update());
    }

    @Override
    public Optional<Integer> updateById(UUID id, Restaurant restaurant) {
        return Optional.of(this.jdbcClient.sql("UPDATE gastrosphere.restaurants SET name = :name, address_id = :address_id, address_number = :address_number, address_complement = :address_complement, " +
                        "restaurant_type = :restaurant_type, opening_hours = :opening_hours, owner_id = :owner_id WHERE id = :id")
                .param("id", id)
                .param("name", restaurant.getName())
                .param("address_id", restaurant.getAddressId() != null ? restaurant.getAddressId() : null)
                .param("address_number", restaurant.getAddressNumber())
                .param("address_complement", restaurant.getAddressComplement())
                .param("restaurant_type", restaurant.getRestaurantType())
                .param("opening_hours", restaurant.getOpeningHours())
                .param("owner_id", restaurant.getOwnerId())
                .update());
    }

    @Override
    public Optional<Integer> deleteById(UUID id) {
        return Optional.of(this.jdbcClient.sql("DELETE FROM gastrosphere.restaurants WHERE id = :id")
                .param("id", id)
                .update());
    }

}
