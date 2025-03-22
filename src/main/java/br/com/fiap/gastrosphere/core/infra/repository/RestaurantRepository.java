package br.com.fiap.gastrosphere.core.infra.repository;

import br.com.fiap.gastrosphere.core.infra.model.RestaurantModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantModel, UUID> {

    Optional<RestaurantModel> findByAddressId(UUID id);
}
