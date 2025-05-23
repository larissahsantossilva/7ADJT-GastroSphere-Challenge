package br.com.fiap.gastrosphere.core.infra.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.fiap.gastrosphere.core.infra.model.MenuModel;

@Repository
public interface MenuRepository extends JpaRepository<MenuModel, UUID> {
	
	@Query("SELECT DISTINCT m FROM MenuModel m LEFT JOIN FETCH m.itemsMenu")
	List<MenuModel> findAllWithItemsMenu();
}
