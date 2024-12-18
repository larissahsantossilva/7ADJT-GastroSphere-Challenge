package br.com.fiap.gastrosphere.repositories;

import br.com.fiap.gastrosphere.entities.User;

import java.util.List;

public interface UserRepository {
    List<User> findAll(int size, int offset);
}
