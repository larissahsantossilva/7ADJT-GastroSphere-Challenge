package br.com.fiap.gastrosphere.repositories;

import br.com.fiap.gastrosphere.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
	
    List<User> findAll(int size, int offset);

    Optional<User> findById(UUID id);

    Optional<Integer> deleteById(UUID id);
}
