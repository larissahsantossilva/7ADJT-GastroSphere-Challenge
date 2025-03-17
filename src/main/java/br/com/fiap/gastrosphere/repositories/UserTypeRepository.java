package br.com.fiap.gastrosphere.repositories;

import br.com.fiap.gastrosphere.entities.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserTypeRepository extends JpaRepository<UserType, UUID> {

    UserType findByName(String name);
}
