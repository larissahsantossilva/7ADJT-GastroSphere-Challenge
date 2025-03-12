package br.com.fiap.gastrosphere.repositories;

import br.com.fiap.gastrosphere.entities.RestaurantType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RestaurantTypeRepository extends JpaRepository<RestaurantType, UUID> {

}
