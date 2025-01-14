package br.com.fiap.gastrosphere.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.fiap.gastrosphere.dtos.UserDto;
import br.com.fiap.gastrosphere.entities.User;

public interface UserRepository {
	
    List<UserDto> findAll(int size, int offset);

    Optional<UserDto> findById(UUID id);

    Integer create(User user);

    Integer update(User user, UUID id);

    Optional<Integer> deleteById(UUID id);

    Integer updatePassword(UUID id, String newPassword);

}
