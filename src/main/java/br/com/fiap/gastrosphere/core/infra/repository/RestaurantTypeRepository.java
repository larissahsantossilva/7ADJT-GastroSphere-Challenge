package br.com.fiap.gastrosphere.core.infra.repository;

import br.com.fiap.gastrosphere.core.infra.model.RestaurantTypeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RestaurantTypeRepository extends JpaRepository<RestaurantTypeModel, UUID> {
}