package br.com.fiap.gastrosphere.core.domain.gateway.user;

import br.com.fiap.gastrosphere.core.domain.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserInterface {

    List<User> findAll();

    User save(User user);

    Optional<User> findById(UUID id);

    void delete(UUID id);

}
