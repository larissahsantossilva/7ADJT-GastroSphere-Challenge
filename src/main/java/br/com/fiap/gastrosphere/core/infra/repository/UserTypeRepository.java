package br.com.fiap.gastrosphere.core.infra.repository;

import br.com.fiap.gastrosphere.core.infra.model.UserTypeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserTypeRepository extends JpaRepository<UserTypeModel, UUID> {

    UserTypeModel findByName(String name);
}
