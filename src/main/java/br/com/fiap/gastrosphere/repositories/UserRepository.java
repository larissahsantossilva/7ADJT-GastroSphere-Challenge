package br.com.fiap.gastrosphere.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.fiap.gastrosphere.dtos.responses.UserBodyResponse;
import br.com.fiap.gastrosphere.entities.User;

public interface UserRepository {
	
    List<UserBodyResponse> findAll(int size, int offset);

    Optional<UserBodyResponse> findById(UUID id);

    Optional<UserBodyResponse> findByAddressId(UUID id);

    Optional<Integer> create(User user);

    Optional<Integer> update(User user, UUID id);

    Optional<Integer> deleteById(UUID id);

    Integer updatePassword(UUID id, String newPassword);

}
