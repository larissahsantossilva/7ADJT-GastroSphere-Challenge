package br.com.fiap.gastrosphere.core.infra.repository;

import br.com.fiap.gastrosphere.core.infra.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {

    Optional<Restaurant> findByAddressId(UUID id);
}
