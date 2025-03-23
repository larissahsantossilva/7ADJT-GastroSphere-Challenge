package br.com.fiap.gastrosphere.core.infra.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiap.gastrosphere.core.infra.model.MenuItemModel;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItemModel, UUID> {

}
