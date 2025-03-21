package br.com.fiap.gastrosphere.core.domain.gateway.usertype;

import br.com.fiap.gastrosphere.core.domain.entity.UserType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserTypeInterface {

    List<UserType> findAll();

    Optional<UserType> findByName(String name);

    UserType save(UserType userType);

    Optional<UserType> findById(UUID id);

    void delete(UUID id);

}
