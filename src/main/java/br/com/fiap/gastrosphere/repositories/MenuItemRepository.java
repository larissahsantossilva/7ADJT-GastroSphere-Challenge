package br.com.fiap.gastrosphere.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiap.gastrosphere.entities.MenuItem;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, UUID> {

}
